package com.project.api;

import com.project.api.support.TestSecurityConfiguration;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestSecurityConfiguration.class)
class HealthCheckIntegrationTest {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @LocalServerPort
    private int port;

    @Test
    void publicHealthIsAccessibleWithoutAuthentication() throws Exception {
        HttpResponse<String> response = send("/api/v1/health", null);

        org.assertj.core.api.Assertions.assertThat(response.statusCode()).isEqualTo(200);
        org.assertj.core.api.Assertions.assertThat(response.body()).contains("\"status\":\"UP\"");
        org.assertj.core.api.Assertions.assertThat(response.body()).contains("\"applicationName\":\"project-api-server\"");
        org.assertj.core.api.Assertions.assertThat(response.body()).contains("\"checkedAt\":");
    }

    @Test
    void securedHealthRejectsUnauthenticatedRequest() throws Exception {
        HttpResponse<String> response = send("/api/v1/health/secured", null);

        org.assertj.core.api.Assertions.assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    void securedHealthReturnsJwtClaimsWhenAuthenticated() throws Exception {
        HttpResponse<String> response = send("/api/v1/health/secured", "integration-token");

        org.assertj.core.api.Assertions.assertThat(response.statusCode()).isEqualTo(200);
        org.assertj.core.api.Assertions.assertThat(response.body()).contains("\"status\":\"UP\"");
        org.assertj.core.api.Assertions.assertThat(response.body()).contains("\"authenticated\":true");
        org.assertj.core.api.Assertions.assertThat(response.body()).contains("\"subject\":\"user-123\"");
        org.assertj.core.api.Assertions.assertThat(response.body()).contains("\"email\":\"tester@example.com\"");
        org.assertj.core.api.Assertions.assertThat(response.body()).contains("\"issuer\":\"http://localhost:8080\"");
        org.assertj.core.api.Assertions.assertThat(response.body()).contains("\"tokenIssuedAt\":\"2026-03-16T00:00:00Z\"");
        org.assertj.core.api.Assertions.assertThat(response.body()).contains("\"tokenExpiresAt\":\"2026-03-16T00:30:00Z\"");
    }

    private HttpResponse<String> send(String path, String bearerToken) throws IOException, InterruptedException {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(java.net.URI.create("http://localhost:" + port + path))
                .GET();

        if (bearerToken != null) {
            requestBuilder.header("Authorization", "Bearer " + bearerToken);
        }

        return httpClient.send(requestBuilder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
