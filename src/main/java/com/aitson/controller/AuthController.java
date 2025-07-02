package com.aitson.controller;

import com.aitson.dto.AuthRequest;
import com.aitson.dto.AuthResponse;
import com.aitson.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Authentication Controller for handling Firebase ID token verification
 * and JWT token generation
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "Authentication endpoints for Firebase ID token verification and JWT generation")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/verify")
    @Operation(
        summary = "Verify Firebase ID Token",
        description = "Verifies a Firebase ID token and returns a JWT token for authenticated access to the application.",
        operationId = "verifyFirebaseToken"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Token verified successfully and JWT returned",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(
                    name = "Successful Verification",
                    value = """
                        {
                          "success": true,
                          "jwt": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
                          "user": {
                            "uid": "firebase_user_123",
                            "email": "user@example.com",
                            "displayName": "John Doe"
                          },
                          "expiresIn": 3600
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request - missing or malformed ID token",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(
                    name = "Invalid Token",
                    value = """
                        {
                          "success": false,
                          "error": "INVALID_TOKEN",
                          "message": "The provided ID token is invalid or expired"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized - token verification failed",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(
                    name = "Verification Failed",
                    value = """
                        {
                          "success": false,
                          "error": "VERIFICATION_FAILED",
                          "message": "Token verification failed"
                        }
                        """
                )
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error during token verification",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthResponse.class),
                examples = @ExampleObject(
                    name = "Server Error",
                    value = """
                        {
                          "success": false,
                          "error": "INTERNAL_ERROR",
                          "message": "An internal server error occurred"
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<AuthResponse> verifyToken(@Valid @RequestBody AuthRequest request) {
        try {
            AuthResponse response = authService.verifyFirebaseToken(request.getIdToken());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setSuccess(false);
            errorResponse.setError("VERIFICATION_FAILED");
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        }
    }

    @GetMapping("/status")
    @Operation(
        summary = "Get Authentication Status",
        description = "Returns the current authentication status and configuration information.",
        operationId = "getAuthStatus"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Authentication status retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Object.class),
                examples = @ExampleObject(
                    name = "Auth Status",
                    value = """
                        {
                          "status": "ACTIVE",
                          "provider": "Firebase",
                          "jwtExpiration": 3600,
                          "features": ["token_verification", "jwt_generation"]
                        }
                        """
                )
            )
        )
    })
    public ResponseEntity<Object> getAuthStatus() {
        return ResponseEntity.ok(Map.of(
            "status", "ACTIVE",
            "provider", "Firebase",
            "jwtExpiration", 3600,
            "features", List.of("token_verification", "jwt_generation")
        ));
    }
} 