package com.project.api.presentation.health.dto;

import java.time.Instant;

public record PublicHealthResponse(
        String status,
        String applicationName,
        Instant checkedAt
) {
}
