package org.pinger.ping;

import org.junit.jupiter.api.Test;
import org.pinger.AbstractTest;
import org.pinger.model.PingResult;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class ICMPPingTest extends AbstractTest {

    private final int timeout = 1000;
    private PingResult result;
    private ICMPPing icmpPing;

    @Test
    void call_Success() {
        // arrange
        result = new PingResult("localhost");   // localhost would always be pingable
        icmpPing = new ICMPPing(result, timeout);
        // act
        boolean isSuccess = icmpPing.call();
        // assert
        assertTrue(isSuccess);
        assertTrue(result.toJson().contains("Lost = 0"));
    }

    @Test
    void call_Failure() {
        // arrange
        result = new PingResult("nonexistent.host");  // Simulating an unreachable host
        icmpPing = new ICMPPing(result, timeout);
        // act
        boolean isFailure = icmpPing.call();
        // assert
        assertFalse(isFailure);
        assertTrue(result.toJson().contains("Ping request could not find host nonexistent.host"));
    }
}