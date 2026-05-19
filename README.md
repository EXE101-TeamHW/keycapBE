# keycapDesignBE

Minimal Spring Boot backend for the Keycap Custom System SRS.

## Requirements
- Java 21
- SQL Server (for local run)

## Configure SQL Server
Update `src/main/resources/application.properties` with your SQL Server credentials.

## Configure JWT + Google OAuth2
Update `src/main/resources/application.properties`:
- `jwt.secret` (>= 32 chars)
- `spring.security.oauth2.client.registration.google.client-id`
- `spring.security.oauth2.client.registration.google.client-secret`

## Run
```cmd
mvnw.cmd spring-boot:run
```

## Test
```cmd
mvnw.cmd test
```

## Swagger
- UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Google Login
- Start OAuth2: http://localhost:8080/oauth2/authorization/google
- On success, server returns JSON `AuthResponse` with JWT token in body.

## Upload Image (Cloudinary)
- Endpoint: `POST /api/uploads`
- Form-data: `file`
- Response: `{ url, publicId }`

## Email Verification
- Register: `POST /api/auth/register` sends a 6-digit code to email
- Verify: `POST /api/auth/verify`
- Resend: `POST /api/auth/resend`

## Notes
- This implementation uses simple request-based auth (userId in requests). JWT is not implemented yet.
- Images and annotations are stored as JSON strings in DB.



