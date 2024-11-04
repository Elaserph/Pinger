package org.pinger.monitor;

import org.pinger.model.Config;
import org.pinger.model.PingResult;
import org.pinger.ping.HTTPPing;
import org.pinger.ping.ICMPPing;
import org.pinger.ping.TraceRoute;
import org.pinger.util.LoggerUtil;

import java.util.concurrent.*;
import java.util.logging.Logger;

public class PingMonitor {

    private static final Logger defaultLogger = Logger.getLogger(PingMonitor.class.getName());
    private static final Logger logger = LoggerUtil.getLogger();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(12); // thread pool limited to 12
    private final Config userConfig;

    static {
        // hook to kill all processes/threads gracefully on application termination
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                scheduler.shutdown();
                if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }));
    }

    public PingMonitor(Config userConfig) {
        this.userConfig = userConfig;
    }

    public void startTasks() {
        for (String host : userConfig.getHosts()) {
            defaultLogger.info("Ping starts for host: " + host);
            PingResult emptyResult = new PingResult(host); // ping processes will store their results in this object for a given host
            // scheduling the hosts for monitoring via ping with a user inputted periodic delay
            scheduler.scheduleWithFixedDelay(() -> runScheduledTasks(emptyResult), 0, userConfig.getDelay(), TimeUnit.SECONDS);
        }
    }

    private void runScheduledTasks(PingResult result) {
        // run ICMP, HTTP, and Trace Route tasks concurrently using CompletableFuture
        CompletableFuture<Boolean> icmpFuture = CompletableFuture.supplyAsync(() -> new ICMPPing(result, userConfig.getIcmpTimeout()).call());
        CompletableFuture<Boolean> httpFuture = CompletableFuture.supplyAsync(() -> new HTTPPing(result, userConfig.getHTTPTimeout(), userConfig.getMaxResponseTime()).call());
        CompletableFuture<Boolean> traceFuture = CompletableFuture.supplyAsync(() -> new TraceRoute(result, userConfig.getTraceTimeout()).call());

        // wait for all tasks to complete before checking if reporting needs to be sent or not
        CompletableFuture.allOf(icmpFuture, httpFuture, traceFuture)
                .thenRun(() -> {
                    try {
                        Boolean icmpResult = icmpFuture.get();
                        Boolean httpResult = httpFuture.get();

                        // check if we should send a report, as per requirements,
                        // only ICMP or HTTP/TCP failure should trigger report
                        if (Boolean.FALSE.equals(icmpResult) || Boolean.FALSE.equals(httpResult)) {
                            new Report().sendReport(result.toJson());
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        logger.warning("Error collecting results for host: " + result.getHost() + " " + e.getMessage());
                    }
                    defaultLogger.info("Ping ends for host: " + result.getHost());
                });
    }
}
