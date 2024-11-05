package org.pinger.monitor;

import org.pinger.util.LoggerUtil;

import java.io.IOException;
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

    public boolean sendReport(String jsonBody) {
        try {
            int responseCode = getHttpResponse(jsonBody);
            // assuming on success, will get response code 200.
            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.severe("Something went wrong, response code: " + responseCode + " \n while sending report" + jsonBody);
                return false;
            }
            else {
                logger.info("Report sent successfully: " + jsonBody);
                return true;
            }
        } catch (Exception e) {
            logger.warning("Error during reporting: " + e.getMessage());
            return false;
        }
    }

    public int getHttpResponse(String jsonBody) throws IOException {
        URL url = new URL(reportURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection.getResponseCode();
    }
}
