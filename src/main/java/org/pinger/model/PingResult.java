package org.pinger.model;

import java.io.Serializable;

public class PingResult implements Serializable {
    private final String host;
    private String icmpResult = "ICMP ping not initialized or completed for given host yet";
    private String tcpResult = "TCP/IP/HTTP ping not initialized or completed for given host yet";
    private String traceResult = "Trace Route not initialized or completed for given host yet";

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
