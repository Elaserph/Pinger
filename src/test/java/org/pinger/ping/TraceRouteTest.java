package org.pinger.ping;

import org.junit.jupiter.api.Test;
import org.pinger.model.PingResult;

import static org.junit.jupiter.api.Assertions.assertTrue;


class TraceRouteTest {

    private final int timeout = 1000;
    private PingResult result;
    private TraceRoute traceRoute;

    @Test
    void testCall_Success() {
        // arrange
        result = new PingResult("localhost");   // localhost would always be traceable
        traceRoute = new TraceRoute(result, timeout);
        // act
        boolean isSuccess = traceRoute.call();
        // assert
        assertTrue(isSuccess);
        assertTrue(result.toJson().contains("Trace complete."));
    }

    @Test
    void testCall_Failure() {
        // arrange
        result = new PingResult("nonexistent.host");    // Simulating an unreachable host
        traceRoute = new TraceRoute(result, timeout);
        // act
        boolean isFailure = traceRoute.call();
        // assert
        assertTrue(isFailure);
        assertTrue(result.toJson().contains("Unable to resolve target system name nonexistent.host"));
    }
}