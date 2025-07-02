package com.aitson.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI Configuration for the Aitson Todo Application
 * Provides comprehensive API documentation with industry best practices
 */
@Configuration
public class OpenApiConfig {

    @Value("${spring.application.name:aitson-todo}")
    private String applicationName;

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers())
                .components(components())
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }

    private Info apiInfo() {
        return new Info()
                .title("Aitson Todo API")
                .description("""
                        A comprehensive REST API for the Aitson Todo application with authentication and health monitoring.
                        
                        ## Features
                        - Firebase ID token verification and JWT generation
                        - Custom health status monitoring
                        - Application information and metadata
                        - Integration with Spring Boot Actuator
                        - Real-time health indicators
                        
                        ## Authentication Endpoints
                        - `POST /api/v1/auth/verify` - Verify Firebase ID token and get JWT
                        - `GET /api/v1/auth/status` - Get authentication status
                        
                        ## Health Endpoints
                        - `GET /api/health/custom` - Get detailed health status
                        - `GET /api/health/info` - Get application information
                        - `GET /actuator/health` - Spring Boot Actuator health endpoint
                        
                        ## Authentication
                        This API uses Firebase ID token verification for secure access.
                        """)
                .version("1.0.0")
                .contact(contact())
                .license(license());
    }

    private Contact contact() {
        return new Contact()
                .name("Aitson Development Team")
                .email("dev@aitson.com")
                .url("https://aitson.com");
    }

    private License license() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    private List<Server> servers() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Local Development Server"),
                new Server()
                        .url("https://api.aitson.com")
                        .description("Production Server")
        );
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes("bearerAuth", bearerAuth())
                .addSecuritySchemes("apiKey", apiKeyAuth());
    }

    private SecurityScheme bearerAuth() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("JWT token for authentication");
    }

    private SecurityScheme apiKeyAuth() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-API-Key")
                .description("API key for external integrations");
    }
} 