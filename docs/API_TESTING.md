# API Testing Guide

This guide explains how to test the backend REST API endpoints using Swagger UI and raw HTTP clients.

---

## 1. Testing Flow
Protected API requests require a valid JWT token. The workflow is:

```text
1. Auth Request  ──► POST /api/v1/auth/login ──► Returns AuthResponse (with JWT token)
2. Copy Token    ──► Copy "token" property value
3. Send Query    ──► Add Header (Authorization: Bearer <token>) ──► Query protected APIs
```

---

## 2. Testing via Swagger UI
Spring Boot runs Swagger documentation. Follow these steps:

1. **Access Swagger**: Open **`http://localhost:8082/swagger-ui.html`** in your browser.
2. **Retrieve JWT**:
   - Locate the **Auth** section and expand `POST /api/v1/auth/login`.
   - Click **Try it out** and enter test credentials (e.g. `rahul_admin` / `adminpassword` or values from the `username` file).
   - Click **Execute**. Copy the token string from the response JSON body.
3. **Authorize Swagger session**:
   - Scroll to the top and click the green **Authorize** button.
   - Enter `Bearer <token>` (replacing `<token>` with your copied JWT token string).
   - Click **Authorize**.
4. **Test Protected Endpoints**:
   - Expand `GET /api/v1/centers`.
   - Click **Try it out** and **Execute** to view live records.

---

## 3. Testing via Postman or Curl
To test endpoints via external HTTP clients, attach the Authorization header:

### Header Key:
`Authorization`

### Header Value:
`Bearer eyJhbGciOiJIUzI1NiJ9...`

### Example Curl Command:
```bash
curl -X GET http://localhost:8082/api/v1/centers \
  -H "Authorization: Bearer <your_jwt_token_here>"
```
