package org.pinger.monitor;

import org.pinger.model.PingResult;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Report {

    private final PingResult result;
    private static final Logger logger = LoggerUtil.getLogger();
    private static final String REPORT_URL = "http://example.com/report"; //Replace with actual reporting URL

    public Report(PingResult result) {
        this.result = result;
    }

    public void sendReport() {
        try {
            System.out.println("Reporting starts for host: " + result.getHost());
            URL url = new URL(REPORT_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = result.toString();
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode != 200)
                logger.severe("Something went wrong: " + result);
            System.out.println("Reporting ends for host: " + result.getHost());
        } catch (Exception e) {
            logger.warning("Error during reporting: " + e.getMessage());
        }
    }
}
