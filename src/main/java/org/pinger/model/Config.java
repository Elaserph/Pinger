package org.pinger.model;

import java.util.List;

/**
 * Configuration class to hold the user-defined configurations for the Ping Monitor application.
 */
public class Config {

    private List<String> hosts;
    private int delay;
    private int icmpTimeout;
    private int httpTimeout;
    private int maxResponseTime;
    private int traceTimeout;
    private String reportURL;

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getHTTPTimeout() {
        return httpTimeout;
    }

    public String getReportURL() {
        return reportURL;
    }

    public void setReportURL(String reportURL) {
        this.reportURL = reportURL;
    }

    public int getIcmpTimeout() {
        return icmpTimeout;
    }

    public void setIcmpTimeout(int icmpTimeout) {
        this.icmpTimeout = icmpTimeout;
    }

    public int getTraceTimeout() {
        return traceTimeout;
    }

    public void setTraceTimeout(int traceTimeout) {
        this.traceTimeout = traceTimeout;
    }

    public void setHttpTimeout(int httpTimeout) {
        this.httpTimeout = httpTimeout;
    }

    public int getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(int maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    /**
     * Checks whether all necessary properties are set and valid.
     *
     * @return True if all properties are valid, false otherwise.
     */
    public boolean checkProperties() {
        return this.hosts != null && !this.hosts.isEmpty() && this.delay != 0 && this.icmpTimeout != 0
                && this.httpTimeout != 0 && this.maxResponseTime != 0 && this.traceTimeout != 0 && this.reportURL != null;
    }

}
