package com.aitson.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO for user information from Firebase authentication
 */
@Schema(description = "User information from Firebase authentication")
public class UserInfo {

    @Schema(description = "Firebase user ID", example = "firebase_user_123")
    private String uid;

    @Schema(description = "User email address", example = "user@example.com")
    private String email;

    @Schema(description = "User display name", example = "John Doe")
    private String displayName;

    @Schema(description = "User profile photo URL", example = "https://example.com/photo.jpg")
    private String photoUrl;

    @Schema(description = "Whether the user email is verified", example = "true")
    private Boolean emailVerified;

    // Default constructor
    public UserInfo() {}

    // Constructor with required fields
    public UserInfo(String uid, String email) {
        this.uid = uid;
        this.email = email;
    }

    // Constructor with all fields
    public UserInfo(String uid, String email, String displayName, String photoUrl, Boolean emailVerified) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.emailVerified = emailVerified;
    }

    // Getters and Setters
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                ", emailVerified=" + emailVerified +
                '}';
    }
} 