package com.project.api.application.health.port.in;

import com.project.api.application.health.HealthStatusResult;

public interface ReadHealthStatusUseCase {

    HealthStatusResult read();
}
