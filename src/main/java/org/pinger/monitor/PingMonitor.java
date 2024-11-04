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
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(12);
    private final Config userConfiguration;

    static {
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

    public PingMonitor(Config userConfiguration) {
        this.userConfiguration = userConfiguration;
    }

    public void startTasks() {
        for (String host : userConfiguration.getHosts()) {
            defaultLogger.info("Ping starts for host: " + host);
            PingResult result = new PingResult(host);
            scheduler.scheduleWithFixedDelay(() -> runScheduledTasks(result), 0, userConfiguration.getDelay(), TimeUnit.SECONDS);
        }
    }

    private void runScheduledTasks(PingResult result) {
        // Run ICMP, TCP, and Trace Route tasks concurrently using CompletableFuture
        CompletableFuture<Boolean> icmpFuture = CompletableFuture.supplyAsync(() -> new ICMPPing(result, userConfiguration.getIcmpTimeout()).call());
        CompletableFuture<Boolean> httpFuture = CompletableFuture.supplyAsync(() -> new HTTPPing(result, userConfiguration.getHTTPTimeout()).call());
        CompletableFuture<Boolean> traceFuture = CompletableFuture.supplyAsync(() -> new TraceRoute(result, userConfiguration.getTraceTimeout()).call());

        // Wait for all tasks to complete and handle reporting
        CompletableFuture.allOf(icmpFuture, httpFuture, traceFuture)
                .thenRun(() -> {
                    try {
                        Boolean icmpResult = icmpFuture.get();
                        Boolean httpResult = httpFuture.get();

                        // Check if we should send a report
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
