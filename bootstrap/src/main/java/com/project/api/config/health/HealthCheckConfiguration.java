package com.project.api.config.health;

import com.project.api.application.health.HealthStatusService;
import com.project.api.application.health.port.in.ReadHealthStatusUseCase;
import java.time.Clock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HealthCheckConfiguration {

    @Bean
    Clock systemClock() {
        return Clock.systemUTC();
    }

    @Bean
    ReadHealthStatusUseCase readHealthStatusUseCase(
            @Value("${spring.application.name:project-api-server}") String applicationName,
            Clock systemClock
    ) {
        return new HealthStatusService(applicationName, systemClock);
    }
}
