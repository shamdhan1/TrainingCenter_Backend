# API Execution Workflows

This document traces the step-by-step execution path of core application features, mapping exactly how requests flow from the Angular browser client down to the PostgreSQL database and back.

---

## WORKFLOW 1: Get Centers (Reference Module)

```text
Browser User Interface
  │
  ▼
Angular CentersListComponent (centers-list.ts)
  │ Calls centerService.getCenters()
  ▼
Angular CenterService (center.service.ts)
  │ Sends HttpClient.get<ApiResponse<Center[]>>('/api/v1/centers')
  ▼
Angular Dev Proxy / Reverse Proxy
  │ Intercepts request and forwards '/api/*' to http://localhost:8082
  ▼
Spring Boot Tomcat Listener (Port 8082)
  │ Evaluates path and extracts headers
  ▼
Spring Security Filters
  │ Validates Authorization JWT header (Bearer token)
  ▼
CenterController (CenterController.java)
  │ Receives request and invokes centerService.getAllCenters()
  ▼
CenterServiceImpl (CenterServiceImpl.java)
  │ Service layer executes transactional query to database
  ▼
CenterRepository (CenterRepository.java)
  │ Translates JPA methods to native SQL queries
  ▼
PostgreSQL Database
  │ Retrieves matches and returns relational result set
  ▼
Center Entity (Center.java)
  │ Hibernate mapping parses result set into Entity objects
  ▼
CenterMapper (CenterMapper.java)
  │ Maps Center Entity into a clean CenterResponse DTO
  ▼
CenterController
  │ Wraps CenterResponse list in ApiResponse wrapper JSON
  ▼
Spring Boot Tomcat
  │ Returns HTTP 200 Response Payload
  ▼
Angular HttpClient
  │ Resolves Observable next() handler with response DTO
  ▼
CentersListComponent
  │ Updates centers signal property state
  ▼
centers-list.html
  │ Renders table rows dynamically using template directives (*ngFor)
```

---

## WORKFLOW 2: Create Center

```text
Angular Reactive Form
  │ User enters center details and clicks "Submit"
  ▼
CentersListComponent
  │ Validates form group inputs and extracts request values
  ▼
CenterService
  │ Sends HttpClient.post<ApiResponse<Center>>('/api/v1/centers', centerData)
  ▼
Spring Boot Validation Handler
  │ Jakarta @Valid constraints validate request body DTO matching CenterRequest rules
  ▼
CenterController
  │ Receives validated CenterRequest DTO and invokes centerService.createCenter()
  ▼
CenterServiceImpl
  │ Validates business rules (e.g. check for code duplication)
  ▼
CenterMapper
  │ Maps CenterRequest DTO properties into a new Center Entity
  ▼
CenterRepository.save()
  │ Executes database INSERT query
  ▼
PostgreSQL Database
  │ Inserts new row and returns auto-generated primary ID
  ▼
CenterMapper
  │ Converts the updated saved entity into a CenterResponse DTO
  ▼
CenterController
  │ Wraps details in ApiResponse.success()
  ▼
Angular Client
  │ Receives HTTP 201 Created response
  ▼
CentersListComponent
  │ Appends new Center to UI list signals
```

---

## WORKFLOW 3: User Login

```text
Login HTML Form
  │ User enters credentials and clicks "Log In"
  ▼
AuthService
  │ Sends HTTP POST request to '/api/v1/auth/login' with LoginRequest payload
  ▼
AuthController
  │ Receives LoginRequest DTO and calls AuthenticationManager.authenticate()
  ▼
UserDetailsService
  │ Queries UserAccountRepository for username
  ▼
Database / PasswordEncoder
  │ Validates BCrypt hash password match
  ▼
JwtTokenProvider
  │ Generates token payload signed with secret key and roles claim
  ▼
AuthResponse DTO
  │ Wraps JWT token string and role metadata
  ▼
Angular Client
  │ Receives AuthResponse and stores the token locally (localStorage)
  ▼
AuthInterceptor
  │ Reads local token and attaches it to all subsequent HTTP headers
```

---

## WORKFLOW 4: Unauthorized Request (HTTP 401)

```text
Angular client calls protected endpoint (e.g. /api/v1/students)
  │
  ▼
AuthInterceptor
  │ Fails to find token or attaches expired/invalid JWT token
  ▼
Spring Boot JwtAuthenticationFilter
  │ Attempts validation check but token signature fails checks
  ▼
JwtAuthenticationEntryPoint
  │ Intercepts failure and triggers unauthorized handler
  ▼
HTTP 401 Unauthorized Response
  │ Tomcat returns 401 error payload
  ▼
AuthInterceptor (Angular)
  │ Intercepts 401 error response and triggers logout callback
  ▼
Angular Router
  │ Flushes local storage and forces redirection back to '/login'
```

---

## WORKFLOW 5: Forbidden Request (HTTP 403)

```text
Angular client triggers Admin action (e.g. DELETE /api/v1/centers/5)
  │
  ▼
AuthInterceptor
  │ Attaches valid JWT token belonging to STUDENT role
  ▼
Spring Boot Security
  │ Authenticates user but evaluates `@PreAuthorize("hasRole('ADMIN')")`
  ▼
AccessDeniedHandler
  │ Captures permission violation
  ▼
HTTP 403 Forbidden Response
  │ Tomcat returns 403 error status code
  ▼
Angular Error Interceptor / UI
  │ Captures 403 error and displays "Access Denied: Admin permissions required."
```
