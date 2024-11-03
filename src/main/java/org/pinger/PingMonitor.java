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

public class PingMonitor {

    private static List<String> hosts;
    private static int delay;
    private static String logFileName;
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
            logFileName = properties.getProperty("log.filename");
            LoggerUtil.setupLogger(logFileName);
            System.out.println(" hosts " + hosts + " delay " + delay);
        } catch (IOException e) {
            System.out.println("Sorry, unable to find config.properties");
            e.printStackTrace();
        }
    }

    private static void startTasks() {
        for (String host : hosts) {
            PingResult emptyResult = new PingResult(host);
            //scheduler.scheduleAtFixedRate(new ICMPPing(emptyResult), 0, delay, TimeUnit.SECONDS);
            //scheduler.scheduleAtFixedRate(new TraceRoute(emptyResult), 0, delay, TimeUnit.SECONDS);
        }
    }
}