package com.markgrand.healthcheck;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class HealthCheckTest {
    private static final int PORT = 8080;
    private static final String LOCALHOST = "127.0.0.1";

    @Test
    void setInternalHeartbeatMillis() {
        HealthCheck healthCheck = new HealthCheck();
        assertEquals(HealthCheck.DEFAULT_INTERNAL_HEARTBEAT_MILLIS, healthCheck.getInternalHeartbeatMillis());
        healthCheck.setInternalHeartbeatMillis(100);
        assertEquals(100, healthCheck.getInternalHeartbeatMillis());
    }

    @Test
    void happyCase() throws Exception {
        HealthCheck healthCheck = new HealthCheck(PORT);
        healthCheck.start();
        expect(HealthCheck.OK_RESPONSE);
        healthCheck.setInternalHeartbeatMillis(2);
        Thread.sleep(8);
        expect(HealthCheck.BAD_RESPONSE);
        healthCheck.heartbeat();
        expect(HealthCheck.OK_RESPONSE);
        healthCheck.fail();
        expect(HealthCheck.FATAL_RESPONSE);
        healthCheck.stop();
        assertThrows(ConnectException.class, ()->new Socket("127.0.0.1", PORT),
                "Should not accept connection after health check is stopped.");
    }

    private void expect(String expectedResponse) throws Exception {
        try (Socket socket = new Socket(LOCALHOST, PORT)) {
            try (InputStream in = socket.getInputStream()) {
                byte[] buffer = new byte[1000];
                int length = in.read(buffer);
                String actualResponse = new String(buffer, 0, length, StandardCharsets.UTF_8);
                assertEquals(expectedResponse, actualResponse, "Exepected response from healthcheck");
            }
        }
    }
}