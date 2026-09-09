# Architectural Assessment - Student Management System

This document outlines the architectural assessment of the current Student Management System, serving as the blueprint for transforming the codebase into a clean, production-oriented monorepo structure.

---

## A. What is Already Good
1. **Technology Selection**: Standard technology stack using **Java / Spring Boot** on the backend and **Angular** on the frontend, which is widely adopted in enterprise settings.
2. **Database Integration**: Powered by **PostgreSQL** using Hibernate and Spring Data JPA for object-relational mapping.
3. **Security Model**: Uses stateless JWT-based authentication which is standard for modern RESTful architectures.
4. **API Versioning**: Implements `/api/v1/...` prefix structure.
5. **Development Tooling**: Features a local proxy server configuration (`proxy.conf.json`) in the Angular workspace to seamlessly redirect client API requests.

---

## B. What Should Be Retained
1. **Core Domain Model**: The underlying schema entities (Students, Trainers, Courses, Centers, Batches, Enrollments, Payments) represent the required business entities and must be preserved.
2. **REST API Versioning Structure**: Continue using `/api/v1/` prefix for all endpoints.
3. **Stateless Security Flow**: Keep JWT extraction, verification, and loading details in the security filters.
4. **Database Configuration Parameters**: Retain connection capabilities to PostgreSQL.

---

## C. What Should Be Refactored
1. **Backend Package Reorganization**: Reorganize Java classes into distinct, structured packages matching their responsibilities (`config`, `controller`, `service`, `repository`, `entity`, `dto`, `mapper`, `exception`, `security`).
2. **Dependency Injection Pattern**: Replace all field-based injections (`@Autowired` on class fields) with constructor-based injection (using Lombok `@RequiredArgsConstructor` or standard constructors) to support unit testability.
3. **Controller Responsibility Limits**: Remove any direct database querying, DTO-to-entity mapping logic, or business authorization rules from controller endpoints, delegating these entirely to the service layer.
4. **DTO Layer Abstraction**: Avoid returning raw database JPA entities directly from REST endpoints. Introduce request and response DTO models (e.g. `StudentRequest`, `StudentResponse`) and clean mapper helpers.
5. **Angular Module Organization**: Restructure the Angular frontend into a standard modular architecture with `core/` (interceptors, guards), `shared/` (shared elements), and `features/` (page modules).
6. **Angular API Call Services**: Remove the generic, untyped `ApiService` and replace it with distinct, typed feature services (e.g. `StudentService`, `CenterService`) utilizing TypeScript interfaces.
7. **CORS Access Rules**: Tighten Spring Security's CORS config to map specific origin rules instead of permissive wildcard matches (`*`).

---

## D. What is Missing for Production-Oriented Architecture
1. **Centralized Exception Handling**: Missing a `@RestControllerAdvice` exception handler to capture system/validation errors and translate them into a standardized error response JSON structure.
2. **Environment Variable Configuration**: Secrets (JWT keys, database passwords) are hardcoded in `application.properties`. These must reference environment variables.
3. **Configuration Profiles**: Separate dev properties (`application-dev.properties`) from prod properties (`application-prod.properties`).
4. **API Routing Fallback**: Spring MVC needs to handle routing requests for client-side pages (e.g. reloading `/dashboard`) to prevent Tomcat from throwing 404 errors.
5. **Swagger/OpenAPI Details**: Complete OpenAPI documentation describing security headers and model constraints.
6. **Actuator Observability**: Enable health indicator endpoint `/actuator/health` to monitor service dependencies.

---

## E. What Should NOT Be Implemented Yet
1. **Microservices (API Gateways, Eureka, Service Discovery)**: Keep the application as a clean, modular monolith to prioritize readability.
2. **Message Brokers (Kafka, RabbitMQ)**: Do not add distributed messaging patterns.
3. **Kubernetes (K8s Manifests, Helm Charts)**: Too complex for local training.
4. **AI/ML Integrations**: Keep all core features simple and separate; AI topics should remain theoretical.
5. **React Frontend**: Maintain the codebase fully in Angular.

---

## F. Recommended Target Architecture

### Monorepo Structure

```text
Student-Management-System/
├── frontend/
│   ├── src/app/
│   │   ├── core/           <-- Guards, interceptors, services, models
│   │   ├── shared/         <-- Reusable layout items
│   │   └── features/       <-- Auth, dashboard, centers, courses, etc.
│   └── proxy.conf.json
├── src/main/java/com/trainingcenter/
│   ├── config/             <-- Initializers, property configurations
│   ├── controller/         <-- REST Controllers (thin delegates)
│   ├── service/            <-- Interfaces & business logic impls
│   ├── repository/         <-- JPA repositories
│   ├── entity/             <-- Database schemas
│   ├── dto/                <-- Request/Response exchange objects
│   ├── mapper/             <-- Entity mapping utilities
│   ├── exception/          <-- Global REST exception handlers
│   └── security/           <-- Authentication & JWT configuration
├── docs/                   <-- Architecture & workflow documentation
└── pom.xml                 <-- Maven settings (uses exec plugin for Angular)
```

### Request Flow

```text
USER BROWSER (UI)
      │
      ▼
Angular Component
      │
      ▼
Angular Feature Service (e.g. CenterService)
      │
      ▼ (HTTP client / Bearer Token)
Nginx / Angular Dev Proxy
      │
      ▼ (Port 8082 /api/v1/...)
Spring Boot REST API
      │
      ├─► Security Filter (JWT Token Authentication)
      ├─► Controller (Validates Request DTO)
      ├─► Service Layer (Executes rules, Maps DTO <-> Entity, Manages Transactions)
      └─► Repository (PostgreSQL Database Queries)
```
