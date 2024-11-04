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
    private final PingResult result;

    public HTTPPing(PingResult result, int timeout) {
        this.host = result.getHost();
        this.result = result;
        this.timeout = timeout;
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

            String output = "Response Code: " + responseCode + ", Time: " + responseTime + " ms against TimeOut: " + timeout + " ms";
            result.setTcpResult(output);
            logger.info("TCP Ping result for " + host + ": " + output);

            //return true or false based on success condition
            return responseCode == HttpURLConnection.HTTP_OK && responseTime <= timeout;
        } catch (IOException e) {
            String message = "Error during HTTP/TCP ping: " + e.getMessage();
            logger.warning(message);
            result.setTcpResult(message);
            return false;
        }
    }
}
