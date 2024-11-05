package org.pinger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.pinger.util.LoggerUtil;

import java.util.logging.Level;

public abstract class AbstractTest {

    private static Level originalLogLevel; // to store original log level of global logger

    @BeforeAll
    static void disableLoggingForTests() {
        // Disable global util logger to avoid console logging
        originalLogLevel = LoggerUtil.getLogger().getLevel();
        LoggerUtil.getLogger().setLevel(Level.OFF);
    }

    @AfterAll
    static void restoreLoggingAfterTests() {
        // Reset global util logger to the original level
        LoggerUtil.getLogger().setLevel(originalLogLevel);
    }
}
