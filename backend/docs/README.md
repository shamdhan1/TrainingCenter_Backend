# 🎓 Student Management System

A production-oriented full stack monorepo learning application featuring a **Java Spring Boot REST API** backend and an **Angular SPA** frontend.

---

## 🏗️ Technical Architecture
This project is configured as a modular monolith running two separate client/server processes during development and compiling into a unified, self-contained JAR executable in production:

```text
USER BROWSER (UI) ──► Nginx (Reverse Proxy) ──► Angular SPA (Client) ──► Spring Boot API ──► PostgreSQL
```

---

## 📁 Monorepo Workspace Structure
```text
Student-Management-System/
├── frontend/                     <-- Angular Client Application
│   ├── src/app/
│   │   ├── core/                 <-- Guards, interceptors, services, models
│   │   ├── shared/               <-- Reusable UI layouts
│   │   └── features/             <-- Pages (centers, courses, students, dashboard)
│   ├── proxy.conf.json           <-- Local dev proxy
│   └── package.json              <-- NPM build definitions
├── src/main/java/com/trainingcenter/
│   ├── config/                   <-- MVC, Jackson configurations
│   ├── controller/               <-- REST APIs
│   ├── service/                  <-- Business implementations
│   ├── mapper/                   <-- Object mappers (DTO <-> Entity)
│   ├── dto/                      <-- Data transfer requests/responses
│   ├── security/                 <-- Spring Security & JWT filters
│   └── exception/                <-- Global REST exception handlers
├── docs/                         <-- Architecture, guides & API workflow docs
├── pom.xml                       <-- Maven project configurations (runs npm builds)
└── Dockerfile                    <-- Multi-stage build definition
```

---

## 🛠️ Prerequisites
- **Java SE Development Kit**: JDK 17
- **Build Engine**: Maven 3.8+
- **JavaScript Engine**: Node.js v22+ & NPM
- **Database Server**: PostgreSQL

---

## 🚀 Getting Started

### 1. Database Setup
Ensure PostgreSQL is running locally on port `5432` with a database named `student_management_System`.

### 2. Environment Configurations
Create a copy of `.env.example` named `.env` and fill in your local Postgres settings:
```bash
cp .env.example .env
```

### 3. Build & Package (Production Mode)
Compile and build a single, self-contained deployable JAR:
```powershell
mvn clean package -DskipTests
```
This automatically runs `npm install`, triggers the Angular build, and bundles the static compiled files directly into the target JAR.

### 4. Running the Unified App
Start the application server hosting both the APIs and the compiled Angular SPA on port `8082`:
```powershell
java -jar target\backend-0.0.1-SNAPSHOT.jar
```
Navigate to **`http://localhost:8082/`** in your browser!

---

## 💻 Local Development Setup (Separate Client & Server)

### 1. Run Backend:
```powershell
mvn spring-boot:run
```
*(Runs REST APIs on port `8082`)*

### 2. Run Frontend:
```powershell
cd frontend
npm start
```
*(Runs Angular CLI server on port `4200` with local proxy configuration)*

Navigate to **`http://localhost:4200`** in your browser!

---

## 📖 Learning Documentation Index
For details on how the system operates under the hood, explore the files under the `docs/` folder:

1. [docs/ARCHITECTURE_ASSESSMENT.md](file:///d:/antigravity/Student-Management-System/docs/ARCHITECTURE_ASSESSMENT.md): Analysis of current design, target patterns, and refactoring targets.
2. [docs/LEARNING_GUIDE.md](file:///d:/antigravity/Student-Management-System/docs/LEARNING_GUIDE.md): 14-Day Practical Study Roadmap with specific coding exercises.
3. [docs/API_WORKFLOW.md](file:///d:/antigravity/Student-Management-System/docs/API_WORKFLOW.md): Step-by-step request/response lifecycle maps.
4. [docs/API_STANDARDS.md](file:///d:/antigravity/Student-Management-System/docs/API_STANDARDS.md): REST conventions, status codes, and URI parameters.
5. [docs/API_TESTING.md](file:///d:/antigravity/Student-Management-System/docs/API_TESTING.md): Guide to querying protected endpoints using Swagger and Postman.
6. [docs/DATABASE.md](file:///d:/antigravity/Student-Management-System/docs/DATABASE.md): Entity mappings, relationship structures, and Flyway setup recommendations.
7. [docs/PRODUCTION_ARCHITECTURE.md](file:///d:/antigravity/Student-Management-System/docs/PRODUCTION_ARCHITECTURE.md): Nginx hosting config and browser security patterns.
8. [docs/COMMANDS.md](file:///d:/antigravity/Student-Management-System/docs/COMMANDS.md): Command execution guides for Windows.
9. [docs/ARCHITECTURE_CHEATSHEET.md](file:///d:/antigravity/Student-Management-System/docs/ARCHITECTURE_CHEATSHEET.md): Cheatsheet containing vocabulary definitions.
10. [docs/FUTURE_AI_INTEGRATION.md](file:///d:/antigravity/Student-Management-System/docs/FUTURE_AI_INTEGRATION.md): Future patterns for secure AI query integrations.
11. [docs/FUTURE_ARCHITECTURE.md](file:///d:/antigravity/Student-Management-System/docs/FUTURE_ARCHITECTURE.md): Caching and Microservice evolution plans.
12. [docs/FINAL_VERIFICATION.md](file:///d:/antigravity/Student-Management-System/docs/FINAL_VERIFICATION.md): Validation check logs.
