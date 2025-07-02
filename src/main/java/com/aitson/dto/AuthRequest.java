package com.aitson.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for Firebase ID token verification requests
 */
@Schema(description = "Firebase ID token verification request")
public class AuthRequest {

    @NotBlank(message = "ID token is required")
    @Size(min = 10, max = 5000, message = "ID token must be between 10 and 5000 characters")
    @Schema(
        description = "Firebase ID token to be verified",
        example = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjEyMzQ1Njc4OTAiLCJ0eXAiOiJKV1QifQ...",
        required = true
    )
    private String idToken;

    // Default constructor
    public AuthRequest() {}

    // Constructor with idToken
    public AuthRequest(String idToken) {
        this.idToken = idToken;
    }

    // Getters and Setters
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    @Override
    public String toString() {
        return "AuthRequest{" +
                "idToken='" + (idToken != null ? idToken.substring(0, Math.min(idToken.length(), 20)) + "..." : "null") + '\'' +
                '}';
    }
} 