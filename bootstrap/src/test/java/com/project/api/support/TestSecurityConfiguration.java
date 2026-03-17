package com.project.api.support;

import java.time.Instant;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

@TestConfiguration
public class TestSecurityConfiguration {

    @Bean
    @Primary
    JwtDecoder jwtDecoder() {
        return token -> {
            if ("integration-token".equals(token)) {
                return Jwt.withTokenValue(token)
                        .header("alg", "RS256")
                        .subject("user-123")
                        .claim("email", "tester@example.com")
                        .claim("iss", "http://localhost:8080")
                        .issuedAt(Instant.parse("2026-03-16T00:00:00Z"))
                        .expiresAt(Instant.parse("2026-03-16T00:30:00Z"))
                        .build();
            }

            if ("test-context-token".equals(token)) {
                return Jwt.withTokenValue(token)
                        .header("alg", "RS256")
                        .subject("test-user")
                        .claim("iss", "http://localhost:8080")
                        .issuedAt(Instant.parse("2026-03-16T00:00:00Z"))
                        .expiresAt(Instant.parse("2026-03-16T01:00:00Z"))
                        .build();
            }

            throw new JwtException("Unsupported test token");
        };
    }
}
