# Hospital Management System

A REST API for a hospital management system built with **Java 17, Spring Boot 3, Spring Security (JWT), Spring Data JPA/Hibernate, and PostgreSQL**.

Covers: patient registration, doctor management, appointment scheduling, medical records, billing, and role-based authentication (ADMIN / DOCTOR / STAFF).

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.2.5 |
| Security | Spring Security + JWT (jjwt) |
| Persistence | Spring Data JPA + Hibernate |
| Database | PostgreSQL 14+ |
| Build tool | Maven |
| API docs | springdoc-openapi (Swagger UI) |

---

## Project Structure

```
src/main/java/com/hospital/management/
├── config/           # SecurityConfig, DataSeeder (seeds default admin)
├── security/         # JwtUtil, JwtAuthFilter, CustomUserDetailsService
├── entity/           # User, Patient, Doctor, Appointment, MedicalRecord, Billing
├── repository/       # Spring Data JPA repositories
├── service/           # Business logic
├── controller/        # REST endpoints
├── dto/auth/          # Login/Register/AuthResponse DTOs
├── exception/          # Global exception handling
└── HospitalManagementApplication.java
```

---

## Prerequisites

- **JDK 17+**
- **Maven 3.8+**
- **PostgreSQL 14+** running locally (or update the connection string for a remote instance, e.g. Supabase/RDS/Neon)

## Setup

1. **Create the database** (required — unlike MySQL, Postgres won't auto-create it from the JDBC URL):
   ```sql
   CREATE DATABASE hospital_db;
   ```
   From the command line: `createdb hospital_db` (or via `psql -U postgres -c "CREATE DATABASE hospital_db;"`)

2. **Configure credentials** in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/hospital_db
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   ```

3. **Change the JWT secret** for anything beyond local testing:
   ```properties
   jwt.secret=ChangeThisSecretKeyToSomethingLongAndRandomForProductionUse1234567890
   ```

4. **Build and run:**
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The API starts on `http://localhost:8080`.

5. On first startup, a default admin account is seeded automatically:
   ```
   username: admin
   password: admin123
   ```
   **This is a placeholder for local development only.** Log in, then either change this account's password or create a real admin account via `/api/auth/register` (using this account's token) and disable/delete the seeded one, before any real deployment. See `config/DataSeeder.java`.

6. **Swagger UI** (interactive API docs): `http://localhost:8080/swagger-ui.html`

### Environment variables (staging/production)

Every sensitive or environment-specific value in `application.properties` reads from an environment variable first, falling back to a local-dev default if unset. **Always set these explicitly outside local development:**

| Variable | Purpose | Local dev default |
|---|---|---|
| `DB_URL` | JDBC connection string | `jdbc:postgresql://localhost:5432/hospital_db` |
| `DB_USERNAME` | Database username | `postgres` |
| `DB_PASSWORD` | Database password | `postgres` |
| `JWT_SECRET` | Signing key for JWTs — must be long/random | placeholder string (not secret) |
| `JWT_EXPIRATION_MS` | Token lifetime in milliseconds | `86400000` (24h) |
| `DDL_AUTO` | Hibernate schema behavior | `update` — set to `validate` in prod, paired with Flyway/Liquibase migrations |
| `SHOW_SQL` | Log SQL statements | `true` — set to `false` in prod |

Example (Linux/macOS):
```bash
export JWT_SECRET="$(openssl rand -base64 48)"
export DB_PASSWORD="a-real-password"
export DDL_AUTO=validate
export SHOW_SQL=false
mvn spring-boot:run
```

---

## Authentication Flow

1. `POST /api/auth/login` with the seeded admin (or any) credentials → returns a JWT.
2. Send that JWT as `Authorization: Bearer <token>` on every subsequent request.
3. Use the admin account's token to call `POST /api/auth/register` and create DOCTOR/STAFF accounts.

> **`/api/auth/register` is ADMIN-only.** It requires a valid JWT belonging to an ADMIN user — random callers can't self-register as ADMIN/DOCTOR/STAFF. This means the very first call to it must be made using the seeded default admin's token (see below).

### Example: login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "admin",
  "role": "ROLE_ADMIN"
}
```

### Example: register a doctor account (requires an ADMIN token)

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Authorization: Bearer <ADMIN_TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"username":"drsmith","password":"pass123","email":"smith@hospital.com","role":"DOCTOR"}'
```

---

## Role Permissions Summary

| Endpoint group | ADMIN | DOCTOR | STAFF |
|---|:---:|:---:|:---:|
| Patients — read | ✅ | ✅ | ✅ |
| Patients — create/update | ✅ | ❌ | ✅ |
| Patients — delete | ✅ | ❌ | ❌ |
| Doctors — read | ✅ | ✅ | ✅ |
| Doctors — create/update/delete | ✅ | ❌ | ❌ |
| Appointments — read | ✅ | ✅ | ✅ |
| Appointments — create/reschedule/delete | ✅ | ❌ | ✅ |
| Appointments — update status | ✅ | ✅ | ✅ |
| Medical records — read/write | ✅ | ✅ | ❌ |
| Medical records — delete | ✅ | ❌ | ❌ |
| Billing — all | ✅ | ❌ | ✅ |
| Billing — delete | ✅ | ❌ | ❌ |

---

## API Reference

### Auth
| Method | Endpoint | Access |
|---|---|---|
| POST | `/api/auth/register` | ADMIN |
| POST | `/api/auth/login` | Public |

### Patients
| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/patients` | ADMIN, DOCTOR, STAFF |
| GET | `/api/patients/{id}` | ADMIN, DOCTOR, STAFF |
| POST | `/api/patients` | ADMIN, STAFF |
| PUT | `/api/patients/{id}` | ADMIN, STAFF |
| DELETE | `/api/patients/{id}` | ADMIN |

### Doctors
| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/doctors` | ADMIN, DOCTOR, STAFF |
| GET | `/api/doctors/{id}` | ADMIN, DOCTOR, STAFF |
| POST | `/api/doctors` | ADMIN |
| PUT | `/api/doctors/{id}` | ADMIN |
| DELETE | `/api/doctors/{id}` | ADMIN |

### Appointments
| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/appointments` | ADMIN, DOCTOR, STAFF |
| GET | `/api/appointments/{id}` | ADMIN, DOCTOR, STAFF |
| GET | `/api/appointments/doctor/{doctorId}` | ADMIN, DOCTOR, STAFF |
| GET | `/api/appointments/patient/{patientId}` | ADMIN, DOCTOR, STAFF |
| POST | `/api/appointments/patient/{patientId}/doctor/{doctorId}` | ADMIN, STAFF |
| PATCH | `/api/appointments/{id}/status/{status}` | ADMIN, STAFF, DOCTOR |
| PUT | `/api/appointments/{id}` | ADMIN, STAFF |
| DELETE | `/api/appointments/{id}` | ADMIN, STAFF |

`status` is one of `SCHEDULED`, `COMPLETED`, `CANCELLED`.

### Medical Records
| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/medical-records/patient/{patientId}` | ADMIN, DOCTOR |
| GET | `/api/medical-records/doctor/{doctorId}` | ADMIN, DOCTOR |
| GET | `/api/medical-records/{id}` | ADMIN, DOCTOR |
| POST | `/api/medical-records/patient/{patientId}/doctor/{doctorId}` | ADMIN, DOCTOR |
| PUT | `/api/medical-records/{id}` | ADMIN, DOCTOR |
| DELETE | `/api/medical-records/{id}` | ADMIN |

### Billing
| Method | Endpoint | Access |
|---|---|---|
| GET | `/api/billing` | ADMIN, STAFF |
| GET | `/api/billing/patient/{patientId}` | ADMIN, STAFF |
| GET | `/api/billing/{id}` | ADMIN, STAFF |
| POST | `/api/billing/patient/{patientId}?appointmentId={optional}` | ADMIN, STAFF |
| PATCH | `/api/billing/{id}/pay` | ADMIN, STAFF |
| DELETE | `/api/billing/{id}` | ADMIN |

---

## Sample Requests (after login, using the token)

**Create a patient**
```bash
curl -X POST http://localhost:8080/api/patients \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","dateOfBirth":"1990-05-14","gender":"Male","email":"john@example.com","phone":"9998887777","address":"123 Main St"}'
```

**Create a doctor**
```bash
curl -X POST http://localhost:8080/api/doctors \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"name":"Dr. Alice Smith","specialization":"Cardiology","email":"alice@hospital.com","phone":"9991112222"}'
```

**Book an appointment** (patient id 1, doctor id 1)
```bash
curl -X POST http://localhost:8080/api/appointments/patient/1/doctor/1 \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"appointmentDateTime":"2026-09-10T10:30:00","reason":"Routine checkup"}'
```

**Add a medical record** (patient id 1, doctor id 1)
```bash
curl -X POST http://localhost:8080/api/medical-records/patient/1/doctor/1 \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"diagnosis":"Common cold","prescription":"Rest, fluids, paracetamol","notes":"Follow up in 1 week if symptoms persist"}'
```

**Create a bill** (patient id 1, linked to appointment id 1)
```bash
curl -X POST "http://localhost:8080/api/billing/patient/1?appointmentId=1" \
  -H "Authorization: Bearer <TOKEN>" \
  -H "Content-Type: application/json" \
  -d '{"amount":150.00}'
```

---

## Frontend

A working React (Vite + Tailwind) frontend now lives in `frontend/`, wired up to this API: login, JWT storage, role-gated navigation, and full pages for Patients, Doctors, Appointments, and Billing.

### Run it

```bash
cd frontend
npm install
npm run dev
```
Opens at `http://localhost:5173`. Log in with the seeded admin account (`admin` / `admin123`) or any account you create.

By default it talks to `http://localhost:8080/api`. To point it elsewhere, copy `.env.example` to `.env` and set `VITE_API_URL`.

### Backend CORS

The backend now allows requests from `http://localhost:5173` by default (see `cors.allowed-origins` in `application.properties`). Add your deployed frontend's URL there (comma-separated) before deploying, via the `CORS_ALLOWED_ORIGINS` environment variable.

---



- **`ManyToOne` relations are `FetchType.EAGER`** on Appointment, MedicalRecord, and Billing (e.g. patient/doctor references) to keep JSON serialization simple without extra Hibernate-Jackson modules. Fine for a project of this size; for a larger system, switch to LAZY + DTO projections to avoid over-fetching.
- **No bidirectional collections** (e.g. `Patient` doesn't hold a `List<Appointment>`) — this avoids infinite-recursion JSON serialization issues and keeps the model simple. Query appointments/records/billing by patient/doctor id via their own endpoints instead.
- **Passwords** are hashed with BCrypt before storage; never stored in plain text.
- **JWT** is stateless — no server-side session, so scale-out is straightforward. Token expiry is configurable via `jwt.expiration-ms`.
- **`ddl-auto=update`** is convenient for development; use a migration tool (Flyway/Liquibase) with `ddl-auto=validate` for production.

## Suggested Next Steps

- Add Flyway/Liquibase migrations instead of `ddl-auto=update`.
- Add pagination (`Pageable`) to list endpoints once data grows.
- Add integration tests with Testcontainers (spin up a real MySQL for tests).
- Restrict `/api/auth/register` to ADMIN-only, and add a separate "doctor self-profile" linking flow.
- Add refresh tokens if you want shorter-lived access tokens.
