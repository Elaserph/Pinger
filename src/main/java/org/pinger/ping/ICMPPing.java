package org.pinger.ping;

import org.pinger.model.PingResult;
import org.pinger.monitor.LoggerUtil;
import org.pinger.monitor.Report;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class ICMPPing implements Runnable {
    private final String host;
    private static final Logger logger = LoggerUtil.getLogger();
    private static final int TIMEOUT = 2000; // Timeout for the ping command
    private final PingResult result;

    public ICMPPing(PingResult result) {
        this.host = result.getHost();
        this.result = result;
    }

    @Override
    public void run() {
        try {
            System.out.println("ICMP Ping start for host: " + host);
            Process process = Runtime.getRuntime().exec("ping -n 5 " + host);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            result.setIcmpResult(output.toString());
            logger.info("ICMP Ping result for " + host + ": " + output);

            //Check for packet loss or timeout
            if (!(output.toString().contains("Lost = 0"))) {
                new Report(result).sendReport(); //Trigger reporting on failure
            }
            System.out.println("ICMP Ping ends for host: " + host);
        } catch (Exception e) {
            logger.warning("Error during ICMP ping: " + e.getMessage());
        }
    }
}
