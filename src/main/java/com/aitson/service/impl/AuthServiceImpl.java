package com.aitson.service.impl;

import com.aitson.dto.AuthResponse;
import com.aitson.dto.UserInfo;
import com.aitson.service.AuthService;
import com.aitson.service.JwtService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Implementation of AuthService for Firebase token verification and JWT generation
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private FirebaseAuth firebaseAuth;

    @Autowired
    private JwtService jwtService;

    @Override
    public AuthResponse verifyFirebaseToken(String idToken) throws Exception {
        if (idToken == null || idToken.trim().isEmpty()) {
            throw new IllegalArgumentException("ID token cannot be null or empty");
        }

        try {
            logger.info("Verifying Firebase ID token...");
            
            // Verify the Firebase ID token
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            
            logger.info("Firebase token verified successfully for user: {}", decodedToken.getUid());

            // Extract user information from Firebase token
            UserInfo userInfo = extractUserInfoFromFirebaseToken(decodedToken);
            
            // Generate JWT token
            String jwtToken = jwtService.generateToken(userInfo);
            
            logger.info("JWT token generated successfully for user: {}", userInfo.getUid());

            // Return successful response
            return AuthResponse.success(jwtToken, userInfo, (int) jwtService.getExpirationTime());
            
        } catch (FirebaseAuthException e) {
            logger.error("Firebase token verification failed: {}", e.getMessage());
            throw new Exception("Invalid Firebase ID token: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during token verification: {}", e.getMessage());
            throw new Exception("Token verification failed: " + e.getMessage());
        }
    }

    /**
     * Extract user information from Firebase token
     */
    private UserInfo extractUserInfoFromFirebaseToken(FirebaseToken decodedToken) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUid(decodedToken.getUid());
        userInfo.setEmail(decodedToken.getEmail());
        userInfo.setEmailVerified(decodedToken.isEmailVerified());
        
        // Extract display name from claims
        String displayName = null;
        if (decodedToken.getClaims() != null && decodedToken.getClaims().containsKey("name")) {
            displayName = (String) decodedToken.getClaims().get("name");
        }
        userInfo.setDisplayName(displayName);
        
        // Extract photo URL from claims
        String photoUrl = null;
        if (decodedToken.getClaims() != null && decodedToken.getClaims().containsKey("picture")) {
            photoUrl = (String) decodedToken.getClaims().get("picture");
        }
        userInfo.setPhotoUrl(photoUrl);
        
        return userInfo;
    }
} 