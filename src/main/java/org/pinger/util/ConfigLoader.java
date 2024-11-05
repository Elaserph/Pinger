package org.pinger.util;

import org.pinger.model.Config;
import org.pinger.monitor.Report;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * The ConfigLoader class is responsible for loading configuration settings from a properties file.
 * This class cannot be instantiated.
 */
public final class ConfigLoader {

    private static final Logger logger = LoggerUtil.getLogger();

    // constructor private for utility class
    private ConfigLoader() {
    }

    /**
     * Loads configuration settings from the specified properties file path.
     * Initializes the Config object with parameters read from the file.
     *
     * @param configFilePath The file path of the configuration properties file.
     * @return A Config object populated with settings from the properties file.
     */
    public static Config loadConfig(String configFilePath) {
        Properties properties = new Properties();
        Config config = new Config();

        // config file need to be present in pwd
        try (InputStream input = Files.newInputStream(Paths.get(configFilePath))) {
            properties.load(input);
            // load all configurations into config object
            config.setHosts(Arrays.asList(properties.getProperty("hosts").split(",")));
            config.setDelay(Integer.parseInt(properties.getProperty("delay")));
            config.setIcmpTimeout(Integer.parseInt(properties.getProperty("icmp.timeout")));
            config.setHttpTimeout(Integer.parseInt(properties.getProperty("http.timeout")));
            config.setMaxResponseTime(Integer.parseInt(properties.getProperty("http.maxtime")));
            config.setTraceTimeout(Integer.parseInt(properties.getProperty("trace.timeout")));
            config.setReportURL(properties.getProperty("report.url"));
            String logFileName = properties.getProperty("log.filename");

            // configuration report url and logger
            Report.setReportUrl(config.getReportURL());
            LoggerUtil.setupLogger(logFileName);
            logger.info("Initializing Monitoring for hosts: " + config.getHosts() + " with periodic delay: " + config.getDelay());
        } catch (IOException e) {
            logger.severe("Sorry, unable to find userConfiguration.properties: " + e.getMessage());
        }
        return config;
    }
}
