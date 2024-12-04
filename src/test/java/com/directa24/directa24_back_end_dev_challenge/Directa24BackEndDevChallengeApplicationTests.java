package com.directa24.directa24_back_end_dev_challenge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Directa24BackEndDevChallengeApplicationTests {

    @LocalServerPort
    private int port;

    @Test
    void contextLoads() {
    }

    @Test
    void mainMethodRunsSuccessfully() {
        assertDoesNotThrow(() ->
                Directa24BackEndDevChallengeApplication.main(new String[]{})
        );
    }

    @Test
    void testApplicationStartOnRandomPort() {
        assertTrue(port > 0, "The application should start on a random port");
    }
}

