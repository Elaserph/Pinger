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

public final class ConfigLoader {

    private static final Logger logger = LoggerUtil.getLogger();

    private ConfigLoader() {
    }

    public static Config loadConfig() {
        Properties properties = new Properties();
        Config config = new Config();

        try (InputStream input = Files.newInputStream(Paths.get("../Config.properties"))) {
            properties.load(input);
            config.setHosts(Arrays.asList(properties.getProperty("hosts").split(",")));
            config.setDelay(Integer.parseInt(properties.getProperty("delay")));
            config.setIcmpTimeout(Integer.parseInt(properties.getProperty("icmp.timeout")));
            config.setHttpTimeout(Integer.parseInt(properties.getProperty("http.timeout")));
            config.setTraceTimeout(Integer.parseInt(properties.getProperty("trace.timeout")));
            config.setReportURL(properties.getProperty("report.url"));
            String logFileName = properties.getProperty("log.filename");

            Report.setReportUrl(config.getReportURL());
            LoggerUtil.setupLogger(logFileName);
            logger.info("Initializing Monitoring for hosts: " + config.getHosts() + " with periodic delay: " + config.getDelay());
        } catch (IOException e) {
            logger.severe("Sorry, unable to find userConfiguration.properties: " + e.getMessage());
        }
        return config;
    }
}
