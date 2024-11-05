package org.pinger.util;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Utility class for setting up and managing the logger for the PingMonitor application.
 * This class cannot be instantiated.
 */
public final class LoggerUtil {

    private static final Logger logger = Logger.getLogger("PingMonitorLogger");

    // constructor private for utility class
    private LoggerUtil() {
    }

    /**
     * Sets up the logger with the specified log file name.
     *
     * @param logFileName The name of the log file.
     */
    public static void setupLogger(String logFileName) {
        try {
            // by default log file will be created in pwd
            FileHandler fh = new FileHandler(logFileName, true);
            fh.setFormatter(new SimpleFormatter());
            logger.addHandler(fh);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            logger.severe("Failed to set up logger: " + e.getMessage());
        }
    }

    public static Logger getLogger() {
        return logger;
    }
}
