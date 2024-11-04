package org.pinger.ping;

import org.pinger.model.PingResult;
import org.pinger.monitor.LoggerUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class HTTPPing implements Callable<Boolean> {
    private final String host;
    private static final Logger logger = LoggerUtil.getLogger();
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

            String output = "Response Code: " + responseCode + ", Time: " + responseTime + " ms against timeOut: " + timeout + " ms";
            result.setTcpResult(output);
            logger.info("TCP Ping result for " + host + ": " + output);

            //return true or false based on success condition
            return responseCode == 200 && responseTime <= timeout;
        } catch (IOException e) {
            logger.warning("Error during HTTP/TCP ping: " + e.getMessage());
            result.setTcpResult("Error during HTTP/TCP ping: " + e.getMessage());
            return false;
        }
    }
}
