package com.aitson.service;

import com.aitson.dto.AuthResponse;

/**
 * Service interface for authentication operations
 */
public interface AuthService {

    /**
     * Verifies a Firebase ID token and returns a JWT token
     * 
     * @param idToken The Firebase ID token to verify
     * @return AuthResponse containing JWT token and user information
     * @throws Exception if token verification fails
     */
    AuthResponse verifyFirebaseToken(String idToken) throws Exception;
} 