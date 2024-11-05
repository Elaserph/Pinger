package org.pinger.monitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pinger.AbstractTest;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class ReportTest extends AbstractTest {

    private final String jsonBodyToReport = "{\"host\":\"dummy host\", \"icmp_ping\":\"dummy icmp result\", \"tcp_ping\":\"dummy tcp/http result\", " +
            "\"trace\":\"dummy trace result\"}";
    private Report report;

    @BeforeEach
    void setUp() {
        report = spy(Report.class); // create spy to just stub getHttpResponse() method
        Report.setReportUrl("http://mockserver.com/report");
    }

    @Test
    void sendReport_success() throws IOException {
        // arrange
        when(report.getHttpResponse(jsonBodyToReport)).thenReturn(200);
        // act
        boolean status = report.sendReport(jsonBodyToReport);
        // assert
        assertTrue(status);
    }

    @Test
    void sendReport_failure() throws IOException {
        // arrange
        when(report.getHttpResponse(jsonBodyToReport)).thenReturn(500); // server error
        // act
        boolean status = report.sendReport(jsonBodyToReport);
        // assert
        assertFalse(status);
    }

}