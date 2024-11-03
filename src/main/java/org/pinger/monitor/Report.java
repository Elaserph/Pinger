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
    private static String reportURL;

    public Report(PingResult result) {
        this.result = result;
    }

    public static void setReportUrl(String reportUrl) {
        Report.reportURL = reportUrl;
    }

    public synchronized void sendReport() {
        if (result.getIcmpFlag() && result.getTraceFlag() && result.getIcmpReportFlag()) {
            result.setIcmpFlag(false);
            result.setTraceFlag(false);
            result.setIcmpReportFlag(false);
            executePOSTRequest();
        }
    }

    private void executePOSTRequest() {
        try {
            System.out.println("Reporting starts for host: " + result.getHost());
            URL url = new URL(reportURL);
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
