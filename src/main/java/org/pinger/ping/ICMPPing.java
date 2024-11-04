package org.pinger.ping;

import org.pinger.model.PingResult;
import org.pinger.monitor.LoggerUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class ICMPPing implements Callable<Boolean> {
    private final String host;
    private static final Logger logger = LoggerUtil.getLogger();
    private final int timeout;
    private final PingResult result;

    public ICMPPing(PingResult result, int timeout) {
        this.host = result.getHost();
        this.result = result;
        this.timeout = timeout;
    }

    @Override
    public Boolean call() {
        try {
            System.out.println("ICMP Ping start for host: " + host);
            Process process = Runtime.getRuntime().exec("ping -n 5 -w " + timeout + " " + host);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            result.setIcmpResult(output.toString());
            logger.info("ICMP Ping result for " + host + ": " + output);
            System.out.println("ICMP Ping ends for host: " + host);
            //return true or false based on packet loss or timeout
            return output.toString().contains("Lost = 0");
        } catch (Exception e) {
            logger.warning("Error during ICMP ping: " + e.getMessage());
            return false;
        }
    }
}
