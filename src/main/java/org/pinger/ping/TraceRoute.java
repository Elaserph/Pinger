package org.pinger.ping;

import org.pinger.model.PingResult;
import org.pinger.monitor.LoggerUtil;
import org.pinger.monitor.Report;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class TraceRoute implements Runnable {

    private final String host;
    private static final Logger logger = LoggerUtil.getLogger();
    private final int timeout;
    private final PingResult result;

    public TraceRoute(PingResult result, int timeout) {
        this.host = result.getHost();
        this.result = result;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        try {
            System.out.println("Trace Route start for host: " + host);
            Process process = Runtime.getRuntime().exec("tracert -h 30 -w " + timeout + " " + host);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }

            result.setTraceResult(output.toString());
            logger.info("Trace Route result for " + host + ": " + output);
            System.out.println("Trace Route ends for host: " + host);
        } catch (Exception e) {
            logger.warning("Error during trace route: " + e.getMessage());
            new Report(result).sendReport(); //Trigger reporting on failure
        }
    }
}
