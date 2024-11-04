package org.pinger.model;

import java.io.Serializable;

public class PingResult implements Serializable {

    private final String host;
    private String icmpResult;
    private String tcpResult;
    private String traceResult;

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

    public String toJson() {
        return "{" +
                "\"host\":\"" + host + "\"," +
                "\"icmp_ping\":\"" + icmpResult + "\"," +
                "\"tcp_ping\":\"" + tcpResult + "\"," +
                "\"trace\":\"" + traceResult + "\"" +
                "}";
    }
}
