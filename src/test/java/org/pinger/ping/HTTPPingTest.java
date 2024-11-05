package org.pinger.ping;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.pinger.AbstractTest;
import org.pinger.model.PingResult;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HTTPPingTest extends AbstractTest {

    private final int timeout = 2000;
    private final int maxResponseTime = 2000;
    private PingResult result;
    private HTTPPing httpPing;

    @EnabledIf("isInternetAvailable")  // enable this test only if internet is available
    @Test
    void call_Success() {
        // arrange
        result = new PingResult("google.com");   // google would be reachable over internet
        httpPing = new HTTPPing(result, timeout, maxResponseTime);
        // act
        boolean isSuccess = httpPing.call();
        // assert
        assertTrue(isSuccess);
        assertTrue(result.toJson().contains("Response Code: 200"));
    }

    @Test
    void call_Failure() {
        // arrange
        result = new PingResult("nonexistent.host");  // Simulating an unreachable host
        httpPing = new HTTPPing(result, timeout, maxResponseTime);
        // act
        boolean isFailure = httpPing.call();
        // assert
        assertFalse(isFailure);
        assertTrue(result.toJson().contains("Error during HTTP/TCP ping:"));
    }

    private boolean isInternetAvailable() {
        try {
            Process process = Runtime.getRuntime().exec("ping -n 1 -w 2000 google.com"); // ping google to check if internet available
            int exitCode = process.waitFor();
            return exitCode == 0;   // true if 0
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

}