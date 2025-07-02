# Firebase Integration Setup Guide

This guide will help you set up Firebase integration for the Aitson Todo application.

## Prerequisites

1. A Firebase project
2. Firebase Admin SDK service account key
3. Java 21 or higher
4. Maven

## Step 1: Create Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a project" or select an existing project
3. Follow the setup wizard
4. Note down your **Project ID**

## Step 2: Enable Authentication

1. In your Firebase project, go to **Authentication**
2. Click **Get started**
3. Go to **Sign-in method** tab
4. Enable the authentication providers you want to use (Google, Email/Password, etc.)

## Step 3: Generate Service Account Key

1. In Firebase Console, go to **Project Settings** (gear icon)
2. Go to **Service accounts** tab
3. Click **Generate new private key**
4. Download the JSON file
5. **Important**: Keep this file secure and never commit it to version control

## Step 4: Configure the Application

### Option A: Using Service Account File (Recommended for Development)

1. Copy your downloaded service account JSON file to:
   ```
   src/main/resources/firebase-service-account.json
   ```

2. Update `application-dev.properties`:
   ```properties
   firebase.project.id=your-actual-project-id
   ```

### Option B: Using Environment Variables (Recommended for Production)

1. Set environment variables:
   ```bash
   export GOOGLE_APPLICATION_CREDENTIALS="/path/to/your/service-account-key.json"
   export FIREBASE_PROJECT_ID="your-project-id"
   ```

2. Update `application-prod.properties`:
   ```properties
   firebase.config.path=
   firebase.project.id=${FIREBASE_PROJECT_ID}
   ```

## Step 5: Configure JWT Secret

### Development
Update `application-dev.properties`:
```properties
jwt.secret=your-secure-256-bit-secret-key-for-development
```

### Production
Set environment variable:
```bash
export JWT_SECRET="your-very-secure-256-bit-secret-key-for-production"
```

Update `application-prod.properties`:
```properties
jwt.secret=${JWT_SECRET}
```

## Step 6: Test the Integration

1. Start the application:
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
   ```

2. Get a Firebase ID token from your client application

3. Test the authentication endpoint:
   ```bash
   curl -X POST http://localhost:8080/api/v1/auth/verify \
     -H "Content-Type: application/json" \
     -d '{
       "idToken": "your-firebase-id-token-here"
     }'
   ```

## Security Best Practices

1. **Never commit service account keys** to version control
2. **Use environment variables** for production secrets
3. **Rotate JWT secrets** regularly
4. **Use HTTPS** in production
5. **Implement rate limiting** for authentication endpoints
6. **Monitor authentication logs** for suspicious activity

## Troubleshooting

### Common Issues

1. **"Firebase initialization failed"**
   - Check if service account file exists and is valid
   - Verify project ID matches your Firebase project

2. **"Invalid Firebase ID token"**
   - Ensure the token is from the correct Firebase project
   - Check if the token is expired
   - Verify authentication is enabled in Firebase Console

3. **"JWT generation failed"**
   - Check JWT secret configuration
   - Ensure JWT secret is at least 256 bits (32 characters)

### Debug Mode

Enable debug logging by adding to `application-dev.properties`:
```properties
logging.level.com.aitson=DEBUG
logging.level.com.google.firebase=DEBUG
```

## API Documentation

Once the application is running, you can access:
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## Next Steps

1. Implement proper error handling
2. Add user management features
3. Implement role-based access control
4. Add authentication middleware for protected endpoints
5. Set up monitoring and logging 