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
    private final int timeout;
    private final PingResult result;

    public ICMPPing(PingResult result, int timeout) {
        this.host = result.getHost();
        this.result = result;
        this.timeout = timeout;
    }

    @Override
    public void run() {
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
            result.setIcmpFlag(true);
            logger.info("ICMP Ping result for " + host + ": " + output);

            //Check for packet loss or timeout
            if (!(output.toString().contains("Lost = 0"))) {
                result.setIcmpReportFlag(true);
                new Report(result).sendReport(); //Trigger reporting on failure
            }
            System.out.println("ICMP Ping ends for host: " + host);
        } catch (Exception e) {
            logger.warning("Error during ICMP ping: " + e.getMessage());
        }
    }
}
