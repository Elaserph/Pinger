package org.pinger.ping;

import org.pinger.model.PingResult;
import org.pinger.util.LoggerUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class HTTPPing implements Callable<Boolean> {

    private static final Logger logger = LoggerUtil.getLogger();
    private final String host;
    private final int timeout;
    private final int maxResponseTime;
    private final PingResult result;

    public HTTPPing(PingResult result, int timeout, int maxResponseTime) {
        this.host = result.getHost();
        this.result = result;
        this.timeout = timeout;
        this.maxResponseTime = maxResponseTime;
    }

    @Override
    public Boolean call() {
        try {
            long startTime = System.currentTimeMillis();
            URL url = new URL("http://" + host);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(timeout);
            connection.connect();
            int responseCode = connection.getResponseCode();
            long responseTime = System.currentTimeMillis() - startTime;

            String output = "URL: http://" + host + ", Response Code: " + responseCode + ", Time: " + responseTime + " ms against given TimeOut: " + timeout + " ms";
            result.setTcpResult(output);
            logger.info(output);
            //when false, signal to send report
            return responseCode == HttpURLConnection.HTTP_OK && responseTime <= maxResponseTime;
        } catch (IOException e) { //catch timeout, server unreachable and other errors
            String message = "Error during HTTP/TCP ping: " + e.getMessage();
            logger.warning(message);
            result.setTcpResult(message);
            //signal to send report
            return false;
        }
    }
}
