package org.pinger;

import org.pinger.model.Config;
import org.pinger.monitor.PingMonitor;
import org.pinger.util.ConfigLoader;
import org.pinger.util.LoggerUtil;

import java.util.logging.Logger;

public class SimplePingApplication {

    private static final Logger defaultLogger = Logger.getLogger(SimplePingApplication.class.getName());
    private static final Logger logger = LoggerUtil.getLogger();

    public static void main(String[] args) {
        Config loadedConfig = ConfigLoader.loadConfig();
        if (loadedConfig.checkProperties()) {
            PingMonitor monitor = new PingMonitor(loadedConfig);
            monitor.startTasks();
        } else {
            String warningMessage = "Invalid config properties, please check the properties again!!! For example, timeout or delays cannot be 0 or null";
            defaultLogger.warning(warningMessage);
            logger.warning(warningMessage);
        }
    }
}