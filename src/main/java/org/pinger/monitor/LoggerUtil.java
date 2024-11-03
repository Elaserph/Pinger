package org.pinger.monitor;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public final class LoggerUtil {
    private static final Logger logger = Logger.getLogger("PingMonitorLogger");

    private LoggerUtil(){}

    public static void setupLogger(String logFileName)  {
        try {
            FileHandler fh = new FileHandler("../"+logFileName, true);
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
