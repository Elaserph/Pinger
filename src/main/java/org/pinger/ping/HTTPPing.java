package org.pinger.ping;

import org.pinger.model.PingResult;
import org.pinger.monitor.Report;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

public class HTTPPing implements Runnable {
    private final String host;
    private static final Logger logger = Logger.getLogger(HTTPPing.class.getName());
    private static final int TIMEOUT = 2000; // Timeout for HTTP request
    private final PingResult result;

    public HTTPPing(String host) {
        this.host = host;
        result = new PingResult(host);
    }

    @Override
    public void run() {
        try {
            long startTime = System.currentTimeMillis();
            URL url = new URL("http://" + host);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT);
            connection.connect();
            int responseCode = connection.getResponseCode();
            long responseTime = System.currentTimeMillis() - startTime;

            String output = "Response Code: " + responseCode + ", Time: " + responseTime + " ms";
            result.setTcpResult(output);
            logger.info("TCP Ping result for " + host + ": " + output);

            //Check for errors
            if (responseCode != 200 || responseTime > TIMEOUT) {
                new Report(result).sendReport(); //Trigger reporting on failure
            }
        } catch (IOException e) {
            logger.warning("Error during TCP ping: " + e.getMessage());
            result.setTcpResult(e.getMessage());
            new Report(result).sendReport(); //Trigger reporting on failure
        }
    }
}
