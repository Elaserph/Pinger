package org.pinger.model;

import java.util.List;

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

    public int getDelay() {
        return delay;
    }

    public int getHTTPTimeout() {
        return httpTimeout;
    }

    public String getReportURL() {
        return reportURL;
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

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public void setReportURL(String reportURL) {
        this.reportURL = reportURL;
    }

    public int getMaxResponseTime() {
        return maxResponseTime;
    }

    public void setMaxResponseTime(int maxResponseTime) {
        this.maxResponseTime = maxResponseTime;
    }

    public boolean checkProperties() {
        return this.hosts != null && !this.hosts.isEmpty() && this.delay != 0 && this.icmpTimeout != 0
                && this.httpTimeout != 0 &&  this.maxResponseTime != 0 && this.traceTimeout != 0 && this.reportURL != null;
    }

}
