
# API Response Examples

## Backend Base URL

- Local backend: `http://localhost:8080`
- Android emulator: `http://10.0.2.2:8080`

All endpoints use the `/api/v1` prefix.

## 1. Authentication APIs

### 1.1 Register User

**Endpoint:** `POST /api/v1/auth/register`

**Request body:**

```json
{
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "password": "SecurePass123"
}
```

**Success response:**

HTTP Status: `201 Created`

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "userRole": "SALES_REP",
  "active": true,
  "createdAt": "2026-11-16T10:30:00+05:30"
}
```

**Validation error response:**

HTTP Status: `400 Bad Request`

```json
{
  "status": 400,
  "message": "Validation failed",
  "errors": {
    "email": "Please provide a valid email address",
    "fullName": "Full name is required",
    "password": "Password must be between 8 and 72 characters"
  }
}
```

**Duplicate email response:**

HTTP Status: `409 Conflict`

```json
{
  "status": 409,
  "message": "An account with this email already exists"
}
```

Registration rules:
- Full name is required and cannot exceed 120 characters.
- Email is required, must be valid, and cannot exceed 255 characters.
- Password must contain between 8 and 72 characters.
- Passwords exceeding 72 UTF-8 bytes are rejected.
- New accounts receive the `SALES_REP` role by default.

### 1.2 Login User

**Endpoint:** `POST /api/v1/auth/login`

**Request body:**

```json
{
  "email": "john.doe@example.com",
  "password": "SecurePass123"
}
```

**Success response:**

HTTP Status: `200 OK`

```json
{
  "accessToken": "<JWT_ACCESS_TOKEN>",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "role": "SALES_REP"
}
```

`expiresIn` represents the token lifetime in seconds. The example value is illustrative; the actual value comes from the backend JWT configuration.

**Invalid credentials:**

HTTP Status: `401 Unauthorized`

The API rejects invalid email/password combinations and inactive accounts.

### 1.3 Get Current User

**Endpoint:** `GET /api/v1/auth/me`

**Headers:**

```http
Authorization: Bearer <JWT_ACCESS_TOKEN>
```

**Success response:**

HTTP Status: `200 OK`

```json
{
  "userId": "550e8400-e29b-41d4-a716-446655440000",
  "fullName": "John Doe",
  "email": "john.doe@example.com",
  "role": "SALES_REP"
}
```

**Authentication requirement:**

A valid Bearer token is required. Missing or invalid authentication results in `401 Unauthorized`.

## 2. User Roles

The backend currently supports:

- `ADMIN`
- `MANAGER`
- `SALES_REP`

Newly registered users are assigned `SALES_REP` by default.

## 3. Authentication Flow

1. The Android application sends registration or login credentials.
2. The backend validates the request.
3. On successful login, the backend generates a signed JWT.
4. The Android application receives the access token and user details.
5. Authenticated API requests include the token in the Authorization header.
6. The backend validates the token before providing protected resources.

## 4. Important Notes

- Passwords are stored as BCrypt hashes, not as plain text.
- Emails are normalized to lowercase by the backend.
- JWT tokens have a configurable expiration.
- Never commit actual passwords, database credentials, JWT secrets, or real access tokens.
- Error response formats for authentication failures may depend on Spring Security and Spring Boot configuration.
