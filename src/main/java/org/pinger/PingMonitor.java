package org.pinger;

import org.pinger.model.PingResult;
import org.pinger.monitor.LoggerUtil;
import org.pinger.ping.ICMPPing;
import org.pinger.ping.TraceRoute;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PingMonitor {

    private static List<String> hosts;
    private static int delay;
    private static final Logger logger = LoggerUtil.getLogger();
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(12);

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

    public static void main(String[] args) {
        loadConfig();
        startTasks();
    }

    private static void loadConfig() {
        Properties properties = new Properties();

        try (InputStream input = Files.newInputStream(Paths.get("../config.properties"))) {
            properties.load(input);
            hosts = Arrays.asList(properties.getProperty("hosts").split(","));
            delay = Integer.parseInt(properties.getProperty("delay"));
            String logFileName = properties.getProperty("log.filename");

            LoggerUtil.setupLogger(logFileName);
            logger.info("Initializing Monitoring for hosts: " + hosts + " with delay: " + delay);
        } catch (IOException e) {
            logger.severe("Sorry, unable to find config.properties: " + e.getMessage());
        }
    }

    private static void startTasks() {
        for (String host : hosts) {
            PingResult emptyResult = new PingResult(host);
            scheduler.scheduleAtFixedRate(new ICMPPing(emptyResult), 0, delay, TimeUnit.SECONDS);
            scheduler.scheduleAtFixedRate(new TraceRoute(emptyResult), 0, delay, TimeUnit.SECONDS);
        }
    }
}