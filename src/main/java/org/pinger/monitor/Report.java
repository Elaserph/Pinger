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

    public void sendReport(String jsonBody) {
        try {
            HttpURLConnection connection = getHttpURLConnection(jsonBody);
            int responseCode = connection.getResponseCode();

            // assuming will get response code as 200 on success.
            if (responseCode != HttpURLConnection.HTTP_OK)
                logger.severe("Something went wrong, response code: " + responseCode + " \n while sending report" + jsonBody);
            else
                logger.info("Report sent successfully: " + jsonBody);
        } catch (Exception e) {
            logger.warning("Error during reporting: " + e.getMessage());
        }
    }

    private HttpURLConnection getHttpURLConnection(String jsonBody) throws IOException {
        URL url = new URL(reportURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        return connection;
    }
}
