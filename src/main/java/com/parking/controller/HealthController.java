package com.parking.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Health", description = "Application health check")
public class HealthController {

    private static final String HEALTH_MESSAGE = "Smart Parking Backend Running Successfully";

    @Value("${spring.application.name:smart-parking-backend}")
    private String applicationName;

    @Operation(summary = "Check application health (liveness)")
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> health() {
        return ResponseEntity.ok(HealthResponse.builder()
                .status("UP")
                .message(HEALTH_MESSAGE)
                .application(applicationName)
                .timestamp(LocalDateTime.now())
                .build());
    }
}
