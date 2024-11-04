package org.pinger;

import org.pinger.model.Config;
import org.pinger.monitor.PingMonitor;
import org.pinger.util.ConfigLoader;

public class SimplePingApplication {

    public static void main(String[] args) {
        Config loadedConfig = ConfigLoader.loadConfig();
        PingMonitor monitor = new PingMonitor(loadedConfig);
        monitor.startTasks();
    }
}