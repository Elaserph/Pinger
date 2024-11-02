package org.pinger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class PingMonitor {

    private static List<String> hosts;
    private static int delay;
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(3);

    public static void main(String[] args) {
        System.out.println("Hello world!");
        loadConfig();
    }

    private static void loadConfig() {
        Properties properties = new Properties();

        try (InputStream input = Files.newInputStream(Paths.get("../config.properties"))) {
            properties.load(input);
            hosts = Arrays.asList(properties.getProperty("hosts").split(","));
            delay = Integer.parseInt(properties.getProperty("delay"));
            System.out.println(" hosts " + hosts + " delay " + delay);
        } catch (IOException e) {
            System.out.println("Sorry, unable to find config.properties");
            e.printStackTrace();
        }
    }
}