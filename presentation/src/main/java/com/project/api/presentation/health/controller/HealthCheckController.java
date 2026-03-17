package com.project.api.presentation.health.controller;

import com.project.api.application.health.HealthStatusResult;
import com.project.api.application.health.port.in.ReadHealthStatusUseCase;
import com.project.api.presentation.health.dto.PublicHealthResponse;
import com.project.api.presentation.health.dto.SecuredHealthResponse;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
@RequiredArgsConstructor
public class HealthCheckController {

    private final ReadHealthStatusUseCase readHealthStatusUseCase;

    @GetMapping
    ResponseEntity<PublicHealthResponse> readPublicHealth() {
        HealthStatusResult result = readHealthStatusUseCase.read();

        return ResponseEntity.ok(new PublicHealthResponse(
                result.status(),
                result.applicationName(),
                result.checkedAt()
        ));
    }

    @GetMapping("/secured")
    ResponseEntity<SecuredHealthResponse> readSecuredHealth(@AuthenticationPrincipal Jwt jwt) {
        HealthStatusResult result = readHealthStatusUseCase.read();

        return ResponseEntity.ok(new SecuredHealthResponse(
                result.status(),
                result.applicationName(),
                result.checkedAt(),
                true,
                jwt.getSubject(),
                jwt.getClaimAsString("email"),
                Objects.nonNull(jwt.getIssuer()) ? jwt.getIssuer().toString() : null,
                jwt.getIssuedAt(),
                jwt.getExpiresAt()
        ));
    }
}
