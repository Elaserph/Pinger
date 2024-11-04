package org.pinger.monitor;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Report {

    private static final Logger logger = LoggerUtil.getLogger();
    private static String reportURL;

    public static void setReportUrl(String reportUrl) {
        Report.reportURL = reportUrl;
    }

    public void sendReport(String jsonBody) {
        try {
            URL url = new URL(reportURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = jsonBody;
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != 200)
                logger.severe("Something went wrong: " + jsonBody);
        } catch (Exception e) {
            logger.warning("Error during reporting: " + e.getMessage());
        }
    }
}
