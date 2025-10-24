package com.stacs.cs5031.p3.server;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Integration test class for the {@link ServerApplication}.
 * Tests the Spring context initialization and ensures that all beans are properly created.
 * The @SpringBootTest annotation configures a real application context for testing instead of mocks.
 */
@SpringBootTest
class ServerApplicationTests {

    /**
     * Tests that the Spring application context loads successfully.
     * This test verifies that:
     * 1. The application configuration is valid
     * 2. All required beans can be created
     * 3. There are no circular dependencies
     * 4. Component scanning works correctly
     * 
     * Fails if the application context cannot be initialized for any reason.
     */
    @Test
    void contextLoads() {
        // No assertions needed - test will fail if context fails to load
    }

}
