package org.pinger.ping;

import org.junit.jupiter.api.Test;
import org.pinger.AbstractTest;
import org.pinger.model.PingResult;

import static org.junit.jupiter.api.Assertions.assertTrue;


class TraceRouteTest extends AbstractTest {

    private final int timeout = 1000;
    private PingResult result;
    private TraceRoute traceRoute;

    @Test
    void call_Success() {
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
    void call_Failure() {
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