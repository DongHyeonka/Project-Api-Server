package com.project.api.application.health;

import com.project.api.application.health.port.in.ReadHealthStatusUseCase;
import java.time.Clock;
import java.time.Instant;

public class HealthStatusService implements ReadHealthStatusUseCase {

    private final String applicationName;
    private final Clock clock;

    public HealthStatusService(String applicationName, Clock clock) {
        this.applicationName = applicationName;
        this.clock = clock;
    }

    @Override
    public HealthStatusResult read() {
        return new HealthStatusResult("UP", applicationName, Instant.now(clock));
    }
}
