package org.pinger.ping;

import org.pinger.model.PingResult;
import org.pinger.util.LoggerUtil;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * The HTTPPing class performs an HTTP GET request to a specified host
 * and logs the results.
 */
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

    /**
     * Executes the HTTP GET request and processes the results.
     *
     * @return true if the HTTP request is successful and within the allowed response time, false otherwise.
     */
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

            String output = "URL: http://" + host + ", Response Code: " + responseCode
                    + ", Time: " + responseTime + " ms against given TimeOut: " + timeout + " ms";
            result.setTcpResult(output);
            logger.info(output);

            // false -> signal to send report
            // To be on safer side I am considering all response code < 400 as desirable, we can go exactly for response code == 200 (which I did earlier),
            // but then it would not serve the purpose of just 'monitor via http'. There might be cases when server is up,
            // and we get 1xx (informational) or 3xx (redirection), covering those cases here as 'server alive'.
            // other than this, also checking if response time <= allowed
            return responseCode < HttpURLConnection.HTTP_BAD_REQUEST && responseTime <= maxResponseTime;
        } catch (IOException e) { // catch timeout, server unreachable and other errors
            String message = "Error during HTTP/TCP ping: " + e.getMessage();
            logger.warning(message);
            result.setTcpResult(message);
            // signal to send report
            return false;
        }
    }
}
