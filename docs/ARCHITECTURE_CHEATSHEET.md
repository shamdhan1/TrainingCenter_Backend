# Architectural Cheatsheet

This cheatsheet defines core development terms and concepts used in this full stack application.

---

## 1. Core Architectural Layers

*   **Controller**: The entry point of HTTP traffic. Thin delegate layer that receives client requests, handles validation triggers, calls service layers, and returns response objects.
*   **Service**: The domain brain. Layer containing all business rules, calculations, mappings, data adjustments, and transactional operations.
*   **Repository**: Data Access Object. Maps Java commands into optimized database-specific queries (JPA, SQL).
*   **Entity**: Java representation of a database table schema.
*   **DTO (Data Transfer Object)**: Lightweight Java class used for data exchange between the client (UI) and the backend (API), isolating database internals.
*   **Mapper**: Utility class mapping properties between entities and DTOs.

---

## 2. Security Concepts

*   **Spring Security**: Framework protecting endpoints from unauthorized requests, providing filters for authentication and role validation.
*   **JWT (JSON Web Token)**: Self-contained secure token containing JSON-formatted claims signed with a backend secret key.
*   **Interceptor**: Client-side hook that intercepts outgoing HttpClient calls (e.g. to attach Bearer tokens) or incoming responses.
*   **Guard**: Angular navigation filter protecting client-side pages from unauthorized route access.
*   **CORS (Cross-Origin Resource Sharing)**: Browser security policy blocking client applications from accessing APIs hosted on separate host domains unless permitted by CORS rules.

---

## 3. Communication & Tools

*   **Proxy**: Local server redirecting requests (e.g. Angular redirecting `/api` to port 8082).
*   **Reverse Proxy**: Server routing incoming public internet requests to appropriate inner services (e.g. Nginx redirecting `/api/*` to Spring Boot).
*   **HTTP**: Communication protocol based on client-server request-response patterns.
*   **JSON**: Lightweight data-interchange formatting standard.
*   **PostgreSQL**: High-performance open-source relational database.
*   **Transaction**: Unit of database execution that is atomic, consistent, isolated, and durable (ACID).
*   **Pagination**: Technique of splitting large data response listings into paginated page blocks.
*   **Validation**: Rule-enforcement checks to ensure user input data is correct (e.g. `@NotNull`, `@Email`).
*   **Exception Handler**: Interceptor converting thrown code exceptions into user-friendly JSON error payloads.
