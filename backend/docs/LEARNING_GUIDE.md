# 14-Day Practical Learning Guide

This guide maps out a 14-day study roadmap to understand full stack development principles by exploring the code in this repository.

---

## 📅 Week 1: Core Layers & Architecture

### Day 1: Project Structure
- **Files to Read**:
  - [`pom.xml`](file:///d:/antigravity/Student-Management-System/pom.xml)
  - [`README_SETUP.md`](file:///d:/antigravity/Student-Management-System/README_SETUP.md)
- **Concept**: Monorepo layout combining separate Spring Boot Java code and Angular client code.
- **Trace**: Check where Angular build outputs are packaged inside Maven targets.
- **Exercise**: Add a test property to `pom.xml`.
- **Explain without AI**: The difference between development running ports and unified production artifacts.

### Day 2: Angular -> API Workflow
- **Files to Read**:
  - [`docs/API_WORKFLOW.md`](file:///d:/antigravity/Student-Management-System/docs/API_WORKFLOW.md)
  - [`frontend/proxy.conf.json`](file:///d:/antigravity/Student-Management-System/frontend/proxy.conf.json)
- **Concept**: Local development redirection proxy (CORS bypass).
- **Trace**: A request from the client browser to the Java backend.
- **Exercise**: Change proxy debug logging level.
- **Explain without AI**: Why a proxy is needed during development but not in production.

### Day 3: Controller -> Service -> Repository
- **Files to Read**:
  - [`CenterController.java`](file:///d:/antigravity/Student-Management-System/src/main/java/com/trainingcenter/controller/CenterController.java)
  - [`CenterServiceImpl.java`](file:///d:/antigravity/Student-Management-System/src/main/java/com/trainingcenter/service/impl/CenterServiceImpl.java)
- **Concept**: Controller delegation, Service-centric business logic, Constructor injection.
- **Trace**: Follow `GET /api/v1/centers` from controller method parameter inputs to SQL generation.
- **Exercise**: Implement a new log print inside `createCenter()`.
- **Explain without AI**: Why field injection (`@Autowired` on class fields) is avoided.

### Day 4: DTO & Mapper Layer
- **Files to Read**:
  - [`CenterRequest.java`](file:///d:/antigravity/Student-Management-System/src/main/java/com/trainingcenter/dto/request/CenterRequest.java)
  - [`CenterResponse.java`](file:///d:/antigravity/Student-Management-System/src/main/java/com/trainingcenter/dto/response/CenterResponse.java)
  - [`CenterMapper.java`](file:///d:/antigravity/Student-Management-System/src/main/java/com/trainingcenter/mapper/CenterMapper.java)
- **Concept**: Request/Response insulation patterns.
- **Trace**: DTO property mappings inside the service transaction.
- **Exercise**: Add a new property description field to `CenterResponse`.
- **Explain without AI**: The security reasons for avoiding exposing JPA entities directly via APIs.

### Day 5: Validation & Global Exception Handling
- **Files to Read**:
  - [`SpaWebMvcConfigurer.java`](file:///d:/antigravity/Student-Management-System/src/main/java/com/trainingcenter/config/SpaWebMvcConfigurer.java)
  - [`ResourceNotFoundException.java`](file:///d:/antigravity/Student-Management-System/src/main/java/com/trainingcenter/exception/ResourceNotFoundException.java)
- **Concept**: Jakarta Validation constraints and custom exceptions.
- **Trace**: Capture duplicate code conflicts and throw a `ConflictException`.
- **Exercise**: Add a minimum size validation `@Size(min = 3)` to the center name.
- **Explain without AI**: How Spring MVC redirects routing paths to index.html.

### Day 6: Angular Services & Models
- **Files to Read**:
  - [`center.model.ts`](file:///d:/antigravity/Student-Management-System/frontend/src/app/features/centers/models/center.model.ts)
  - [`center.service.ts`](file:///d:/antigravity/Student-Management-System/frontend/src/app/features/centers/services/center.service.ts)
- **Concept**: Typed Angular Http services.
- **Trace**: The mapping of observable response DTO arrays.
- **Exercise**: Add a clean typing field interface to `Center`.
- **Explain without AI**: Why TypeScript models improve development code quality.

### Day 7: Angular Routing & Components
- **Files to Read**:
  - [`app.routes.ts`](file:///d:/antigravity/Student-Management-System/frontend/src/app/app.routes.ts)
  - [`centers-list.ts`](file:///d:/antigravity/Student-Management-System/frontend/src/app/features/centers/pages/centers-list.ts)
- **Concept**: SPA Routing and Component view models.
- **Trace**: Route navigation triggers inside the sidebar.
- **Exercise**: Create a dummy page component template.
- **Explain without AI**: How Angular manages client-side views without reloading the page.

---

## 📅 Week 2: Security, Configuration & Deployment

### Day 8: JWT Authentication Flow
- **Files to Read**:
  - [`SecurityConfig.java`](file:///d:/antigravity/Student-Management-System/src/main/java/com/trainingcenter/security/SecurityConfig.java)
- **Concept**: Stateless session management.
- **Trace**: Trace how login matches credentials and returns a JWT token.
- **Exercise**: Update password validation in test configurations.
- **Explain without AI**: The structure of a JWT token (Header, Payload, Signature).

### Day 9: Interceptors & Route Guards
- **Concept**: Centralized headers insertion and navigation intercept.
- **Trace**: Attach auth token header signatures to outbound queries.
- **Exercise**: Add a console log output inside an interceptor block.
- **Explain without AI**: The execution sequence of Angular Route Guards.

### Day 10: Role-Based Authorization
- **Concept**: Permission levels matching roles (`ADMIN`, `TRAINER`, `STUDENT`).
- **Trace**: Security intercept checks on DELETE queries.
- **Exercise**: Restrict an endpoint using `@PreAuthorize("hasRole('ADMIN')")`.
- **Explain without AI**: The difference between role authentication and action authorization.

### Day 11: Pagination, Search & Sorting
- **Concept**: Page requests mapping parameters.
- **Trace**: Dynamic paging queries using Spring Data JPA.
- **Exercise**: Alter page size defaults from `10` to `20`.
- **Explain without AI**: The performance benefits of paginating listings.

### Day 12: Logging, Actuator & Swagger
- **Concept**: Operational visibility.
- **Trace**: Request monitoring via Swagger endpoints.
- **Exercise**: Fetch `/actuator/health` from your browser.
- **Explain without AI**: Why logging parameters must exclude sensitive values.

### Day 13: Application Testing
- **Concept**: Component isolation test structures.
- **Trace**: Check database schema isolation matches in mock assertions.
- **Exercise**: Run `mvn test` in the terminal.
- **Explain without AI**: The benefits of unit tests over manual clicks.

### Day 14: Production Architecture & Docker Setup
- **Files to Read**:
  - [`docs/PRODUCTION_ARCHITECTURE.md`](file:///d:/antigravity/Student-Management-System/docs/PRODUCTION_ARCHITECTURE.md)
- **Concept**: Nginx routing limits and Docker configurations.
- **Trace**: Map proxy forwards from port 80 to Spring Boot.
- **Exercise**: Edit environmental variable defaults in `.env.example`.
- **Explain without AI**: The complete unified hosting architecture flow.
