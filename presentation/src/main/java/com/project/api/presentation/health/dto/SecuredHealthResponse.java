package com.project.api.presentation.health.dto;

import java.time.Instant;

public record SecuredHealthResponse(
        String status,
        String applicationName,
        Instant checkedAt,
        boolean authenticated,
        String subject,
        String email,
        String issuer,
        Instant tokenIssuedAt,
        Instant tokenExpiresAt
) {
}
