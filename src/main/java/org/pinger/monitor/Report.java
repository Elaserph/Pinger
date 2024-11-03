package org.pinger.monitor;

import org.pinger.model.PingResult;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Report {

    private final PingResult result;
    private static final Logger logger = Logger.getLogger(Report.class.getName());
    private static final String REPORT_URL = "http://example.com/report"; //Replace with actual reporting URL

    public Report(PingResult result) {
        this.result = result;
    }

    public void sendReport() {
        try {
            URL url = new URL(REPORT_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = result.toString();
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            logger.log(Level.SEVERE, "Something went wrong: {0} ", result);
        } catch (Exception e) {
            logger.warning("Error during reporting: " + e.getMessage());
        }
    }
}
