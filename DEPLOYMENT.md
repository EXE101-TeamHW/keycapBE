# Deploy Keycap Backend

The backend uses Java 21, Spring Boot, PostgreSQL, and Docker. Production
configuration is read from environment variables; do not commit real secrets.

## Run Locally With PowerShell

Create PostgreSQL database `keycap_design`, then run:

```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/keycap_design"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:SPRING_DATASOURCE_PASSWORD="<local-password>"
$env:JWT_SECRET="<at-least-32-random-characters>"
$env:APP_CORS_ALLOWED_ORIGINS="http://localhost:5173"
.\mvnw.cmd spring-boot:run
```

Or run PostgreSQL and the backend with Docker:

```powershell
Copy-Item .env.example .env
# Fill in .env, especially POSTGRES_PASSWORD and JWT_SECRET.
docker compose up --build -d
```

Local endpoints:

```text
http://localhost:8080/actuator/health
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs
```

## Supabase PostgreSQL

1. Create a Supabase project.
2. Open **Project Settings > Database > Connect**.
3. Copy the direct connection details. If DigitalOcean cannot use the direct
   IPv6 connection, use the Supabase session pooler.
4. Require SSL in the JDBC URL:

   ```text
   jdbc:postgresql://<supabase-host>:5432/postgres?sslmode=require
   ```

Keep username and password in separate environment variables. Do not put the
password inside `SPRING_DATASOURCE_URL`.

The first deployment may use `JPA_DDL_AUTO=update` to create tables. For stable
production data, introduce versioned migrations and change it to `validate`.

## DigitalOcean App Platform

Create an App from the Git repository, then add a **Web Service** using the
repository's `Dockerfile`.

Recommended component settings:

```text
Resource size: 2 GB RAM
HTTP port: 8080
Health check path: /actuator/health
```

Set these required runtime environment variables:

```text
SPRING_DATASOURCE_URL=jdbc:postgresql://<supabase-host>:5432/postgres?sslmode=require
SPRING_DATASOURCE_USERNAME=<supabase-user>
SPRING_DATASOURCE_PASSWORD=<supabase-password>
JWT_SECRET=<at-least-32-random-characters>
APP_CORS_ALLOWED_ORIGINS=https://<frontend-domain>
FRONTEND_URL=https://<frontend-domain>
JPA_DDL_AUTO=update
JAVA_TOOL_OPTIONS=-Xms256m -Xmx1200m
```

Set these when their corresponding features are used:

```text
GOOGLE_CLIENT_ID
GOOGLE_CLIENT_SECRET
GEMINI_API_KEY
AI_SYSTEM_PASSWORD
CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET
MAIL_ENABLED
MAIL_USERNAME
MAIL_PASSWORD
PAYOS_CLIENT_ID
PAYOS_API_KEY
PAYOS_CHECKSUM_KEY
PAYOS_RETURN_URL
PAYOS_CANCEL_URL
```

Optional tuning variables:

```text
DB_MAX_POOL_SIZE=10
DB_MIN_IDLE=2
JWT_EXPIRATION_MS=3600000
JPA_SHOW_SQL=false
GEMINI_ENDPOINT=https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent
GEMINI_MODEL=gemini-3.1-flash-lite
GEMINI_TEMPERATURE=0.4
GEMINI_MAX_OUTPUT_TOKENS=1000
GEMINI_THINKING_BUDGET=0
GEMINI_TIMEOUT_MS=10000
AI_PROVIDER_DEBUG=false
AI_SYSTEM_EMAIL=ai-assistant@keycap.local
AI_SYSTEM_NAME=Keycap AI Assistant
AI_MAX_RECOMMENDATIONS=5
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_DEBUG=false
```

DigitalOcean supplies `PORT`; the backend uses `${PORT:8080}`. The Docker image
also exposes port `8080`.

After deployment, verify:

```text
https://<backend-domain>/actuator/health
https://<backend-domain>/swagger-ui.html
```

Configure external callbacks:

```text
Google OAuth callback:
https://<backend-domain>/login/oauth2/code/google

PayOS webhook:
https://<backend-domain>/api/payments/payos/webhook
```

For multiple frontend origins, use a comma-separated value:

```text
APP_CORS_ALLOWED_ORIGINS=https://shop.example.com,https://admin.example.com
```

## Build

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd clean package -DskipTests
docker build -t keycap-backend .
```

## Existing SQL Server Data

The backend schema is PostgreSQL-compatible, but old SQL Server data is not
copied automatically. Migrate it with a dedicated migration tool, then verify
foreign keys, enum text values, and identity sequences before serving traffic.
