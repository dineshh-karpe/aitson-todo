package com.aitson.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

/**
 * Firebase configuration for initializing Firebase Admin SDK
 */
@Configuration
public class FirebaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfig.class);

    @Value("${firebase.config.path:firebase-service-account.json}")
    private String firebaseConfigPath;

    @Value("${firebase.project.id:}")
    private String firebaseProjectId;

    @Bean
    public FirebaseApp firebaseApp() {
        try {
            // Check if Firebase is already initialized
            if (FirebaseApp.getApps().isEmpty()) {
                logger.info("Initializing Firebase Admin SDK...");
                
                // Load Firebase service account credentials
                GoogleCredentials credentials = loadFirebaseCredentials();
                
                // Build Firebase options
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .setProjectId(firebaseProjectId)
                        .build();
                
                // Initialize Firebase app
                FirebaseApp app = FirebaseApp.initializeApp(options);
                logger.info("Firebase Admin SDK initialized successfully for project: {}", firebaseProjectId);
                return app;
            } else {
                logger.info("Firebase Admin SDK already initialized");
                return FirebaseApp.getInstance();
            }
        } catch (Exception e) {
            logger.error("Failed to initialize Firebase Admin SDK", e);
            throw new RuntimeException("Firebase initialization failed", e);
        }
    }

    @Bean
    public FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance(firebaseApp());
    }

    /**
     * Load Firebase service account credentials from classpath
     */
    private GoogleCredentials loadFirebaseCredentials() throws IOException {
        try {
            // Try to load from classpath first
            InputStream serviceAccount = new ClassPathResource(firebaseConfigPath).getInputStream();
            logger.info("Loading Firebase credentials from classpath: {}", firebaseConfigPath);
            return GoogleCredentials.fromStream(serviceAccount);
        } catch (IOException e) {
            logger.warn("Could not load Firebase credentials from classpath: {}", e.getMessage());
            
            // Fallback to default credentials (useful for development)
            logger.info("Using default Google credentials");
            return GoogleCredentials.getApplicationDefault();
        }
    }
} 