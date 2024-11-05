package org.pinger;

import org.pinger.model.Config;
import org.pinger.monitor.PingMonitor;
import org.pinger.util.ConfigLoader;
import org.pinger.util.LoggerUtil;

import java.util.logging.Logger;

/**
 * The SimplePingApplication class is the entry point for the Ping Monitor application.
 *
 * @author <a href="https://github.com/Elaserph">elaserph</a>
 */
public class SimplePingApplication {

    private static final Logger defaultLogger = Logger.getLogger(SimplePingApplication.class.getName());
    private static final Logger logger = LoggerUtil.getLogger();
    private static final String DEFAULT_CONFIG_FILE_PATH = "../config.properties";

    /**
     * Entry point.
     * Loads the user configuration from a specified properties file, validates the configuration,
     * and starts the PingMonitor if the configuration is valid.
     *
     * @param args Command line arguments. The first argument is the path to the configuration file.
     */
    public static void main(String[] args) {
        String configFilePath = (args != null && args.length > 0) ?
                args[0] : DEFAULT_CONFIG_FILE_PATH;

        // load user configurations from config.properties file
        Config loadedConfig = ConfigLoader.loadConfig(configFilePath);
        // check sanity
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