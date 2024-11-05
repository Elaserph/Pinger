package org.pinger.monitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pinger.AbstractTest;
import org.pinger.model.Config;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.*;

class PingMonitorTest extends AbstractTest {

    private Config mockConfig;
    private PingMonitor pingMonitor;

    @BeforeEach
    void setUp() {
        // Create mocks for dependencies
        mockConfig = mock(Config.class);

        // Configure mock behavior for config
        when(mockConfig.getHosts()).thenReturn(Arrays.asList("example.com", "nonexistent.host", "", "  "));
        when(mockConfig.getDelay()).thenReturn(100);
        when(mockConfig.getIcmpTimeout()).thenReturn(2000);
        when(mockConfig.getHTTPTimeout()).thenReturn(2000);
        when(mockConfig.getMaxResponseTime()).thenReturn(1000);
        when(mockConfig.getTraceTimeout()).thenReturn(1000);

        // Initialize PingMonitor with mocked config and logger
        pingMonitor = new PingMonitor(mockConfig);
    }

    @Test
    void startTasks_SchedulesHostsCorrectly() {
        pingMonitor.startTasks();

        // Wait until all threads have completed their tasks before asserting
        await().atMost(1, TimeUnit.SECONDS).untilAsserted(() -> {
            verify(mockConfig, times(1)).getHosts();
            verify(mockConfig, times(2)).getDelay();
            verify(mockConfig, times(2)).getIcmpTimeout();
            verify(mockConfig, times(2)).getHTTPTimeout();
            verify(mockConfig, times(2)).getMaxResponseTime();
            verify(mockConfig, times(2)).getTraceTimeout();
        });
    }

}
