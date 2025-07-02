package com.aitson.dto;

import com.aitson.dto.UserInfo;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for authentication responses
 */
@Schema(description = "Authentication response containing JWT token and user information")
public class AuthResponse {

    @Schema(description = "Whether the authentication was successful", example = "true")
    private Boolean success;

    @Schema(description = "JWT token for authenticated access", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String jwt;

    @Schema(description = "User information from Firebase")
    private UserInfo user;

    @Schema(description = "JWT token expiration time in seconds", example = "3600")
    private Integer expiresIn;

    @Schema(description = "Error code if authentication failed", example = "INVALID_TOKEN")
    private String error;

    @Schema(description = "Error message if authentication failed", example = "The provided ID token is invalid or expired")
    private String message;

    // Default constructor
    public AuthResponse() {}

    // Constructor for successful authentication
    public AuthResponse(Boolean success, String jwt, UserInfo user, Integer expiresIn) {
        this.success = success;
        this.jwt = jwt;
        this.user = user;
        this.expiresIn = expiresIn;
    }

    // Constructor for failed authentication
    public AuthResponse(Boolean success, String error, String message) {
        this.success = success;
        this.error = error;
        this.message = message;
    }

    // Static factory methods
    public static AuthResponse success(String jwt, UserInfo user, Integer expiresIn) {
        return new AuthResponse(true, jwt, user, expiresIn);
    }

    public static AuthResponse failure(String error, String message) {
        return new AuthResponse(false, error, message);
    }

    // Getters and Setters
    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "success=" + success +
                ", jwt='" + (jwt != null ? jwt.substring(0, Math.min(jwt.length(), 20)) + "..." : "null") + '\'' +
                ", user=" + user +
                ", expiresIn=" + expiresIn +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
} 