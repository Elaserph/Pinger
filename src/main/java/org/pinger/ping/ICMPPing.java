package org.pinger.ping;

import org.pinger.model.PingResult;
import org.pinger.util.LoggerUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

/**
 * The ICMPPing class performs an ICMP ping to a specified host
 */
public class ICMPPing implements Callable<Boolean> {

    private static final Logger logger = LoggerUtil.getLogger();
    private final String host;
    private final int timeout;
    private final PingResult result;

    public ICMPPing(PingResult result, int timeout) {
        this.host = result.getHost();
        this.result = result;
        this.timeout = timeout;
    }

    /**
     * Executes the ICMP ping and processes the results.
     *
     * @return true if the ICMP ping is successful, false otherwise.
     */
    @Override
    public Boolean call() {
        try {
            // ping host 5 times with a given timeout
            Process process = Runtime.getRuntime().exec("ping -n 5 -w " + timeout + " " + host);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            String resultString = "ICMP Ping result for " + host + ": " + output;
            result.setIcmpResult(resultString);
            logger.info(resultString);

            // false -> signal to send report
            // "Lost = 0" substring if not present, signify packet loss or timeout
            return resultString.contains("Lost = 0");
        } catch (Exception e) {
            logger.warning("Error during ICMP ping: " + e.getMessage());
            // signal to send report
            return false;
        }
    }
}
