package org.pinger.model;

import java.io.Serializable;

public class PingResult implements Serializable {
    private final String host;
    private String icmpResult = "ICMP ping not initialized or completed for given host yet";
    private String tcpResult = "TCP/IP/HTTP ping not initialized or completed for given host yet";
    private String traceResult = "Trace Route not initialized or completed for given host yet";

    private Boolean icmpFlag = false;
    private Boolean traceFlag = false;
    private Boolean icmpReportFlag = false;

    public PingResult(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setIcmpResult(String icmpResult) {
        this.icmpResult = icmpResult;
    }

    public void setTcpResult(String tcpResult) {
        this.tcpResult = tcpResult;
    }

    public void setTraceResult(String traceResult) {
        this.traceResult = traceResult;
    }

    public synchronized Boolean getTraceFlag() {
        return traceFlag;
    }

    public synchronized void setTraceFlag(Boolean traceFlag) {
        this.traceFlag = traceFlag;
    }

    public synchronized Boolean getIcmpFlag() {
        return icmpFlag;
    }

    public synchronized void setIcmpFlag(Boolean icmpFlag) {
        this.icmpFlag = icmpFlag;
    }

    public synchronized Boolean getIcmpReportFlag() {
        return icmpReportFlag;
    }

    public synchronized void setIcmpReportFlag(Boolean icmpReportFlag) {
        this.icmpReportFlag = icmpReportFlag;
    }

    @Override
    public String toString() {
        return "{" +
                "host='" + host + '\'' +
                ", icmp_ping='" + icmpResult + '\'' +
                ", tcp_ping='" + tcpResult + '\'' +
                ", trace='" + traceResult + '\'' +
                '}';
    }
}
