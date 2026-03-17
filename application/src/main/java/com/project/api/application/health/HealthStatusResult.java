package com.project.api.application.health;

import java.time.Instant;

public record HealthStatusResult(
        String status,
        String applicationName,
        Instant checkedAt
) {
}
