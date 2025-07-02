package com.aitson.controller;

import com.aitson.health.CustomHealthIndicator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Monitoring", description = "Health check and monitoring endpoints for the Aitson Todo application")
public class HealthController {

    @Autowired
    private CustomHealthIndicator customHealthIndicator;

    @GetMapping("/custom")
    @Operation(
        summary = "Get Custom Health Status",
        description = "Retrieves the custom health status of the application including database connectivity, system resources, and custom health indicators.",
        operationId = "getCustomHealth"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Health status retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    name = "Healthy Status",
                    value = """
                        {
                          "status": "UP",
                          "database": "UP",
                          "memory": "UP",
                          "disk": "UP",
                          "timestamp": "2024-01-15T10:30:00Z"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "503",
            description = "Service unavailable - one or more health checks failed",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    name = "Unhealthy Status",
                    value = """
                        {
                          "status": "DOWN",
                          "database": "DOWN",
                          "memory": "UP",
                          "disk": "UP",
                          "timestamp": "2024-01-15T10:30:00Z"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Map<String, Object>> getCustomHealth() {
        return ResponseEntity.ok(customHealthIndicator.getHealthStatus());
    }

    @GetMapping("/info")
    @Operation(
        summary = "Get Health Information",
        description = "Retrieves general information about the health monitoring service including available endpoints and application details.",
        operationId = "getHealthInfo"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Health information retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    name = "Health Info Response",
                    value = """
                        {
                          "application": "Aitson Health Check Service",
                          "version": "1.0.0",
                          "description": "Spring Boot application with comprehensive health monitoring",
                          "actuatorEndpoints": "Available at /actuator/*",
                          "customHealth": "Available at /api/health/custom"
                        }
                        """
                )
            )
        )
    })
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