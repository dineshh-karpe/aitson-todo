package com.aitson.controller;

import com.aitson.health.CustomHealthIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @Autowired
    private CustomHealthIndicator customHealthIndicator;

    @GetMapping("/custom")
    public ResponseEntity<Map<String, Object>> getCustomHealth() {
        return ResponseEntity.ok(customHealthIndicator.getHealthStatus());
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, String>> getHealthInfo() {
        Map<String, String> info = Map.of(
            "application", "Aitson Health Check Service",
            "version", "1.0.0",
            "description", "Spring Boot application with comprehensive health monitoring",
            "actuatorEndpoints", "Available at /actuator/*",
            "customHealth", "Available at /api/health/custom"
        );
        return ResponseEntity.ok(info);
    }
} 