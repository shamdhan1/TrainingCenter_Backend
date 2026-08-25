# 🎓 Student Management System - Setup Guide

This project is restructured as an industry-standard full stack web application featuring an **Angular** frontend and a **Spring Boot (Java)** REST API backend.

---

## 📁 Workspace Directory Structure

```text
Student-Management-System/
├── frontend/                     <-- Angular Application (Client workspace)
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/       <-- Angular Page Components (Dashboard, Centers, etc.)
│   │   │   ├── services/         <-- API Service (Handles REST communication)
│   │   │   └── app.routes.ts     <-- Client-side Navigation Router
│   │   ├── assets/               <-- Local Images / Static assets
│   │   ├── index.html
│   │   └── main.ts
│   ├── proxy.conf.json           <-- Local Development Proxy (Avoids CORS issues)
│   ├── angular.json              <-- Angular Workspace settings
│   └── package.json              <-- NPM Dependencies & Run Scripts
├── src/main/java/                <-- Spring Boot REST API Java Classes
├── src/main/resources/           <-- Spring Boot Application Resources & Properties
├── pom.xml                       <-- Maven Project Dependency Manifest
└── README_SETUP.md               <-- Setup Guide Documentation (This file)
```

---

## 🚀 How to Run the Application

During development, the frontend and backend run as separate local processes on different ports. Communication is routed via a development proxy.

### 1. Start the Java Backend (Spring Boot)
- **Port**: `8082` (REST APIs are exposed under `/api/v1/...`)
- **Command**:
  ```bash
  mvn spring-boot:run
  ```

### 2. Start the Frontend (Angular)
- **Port**: `4200`
- **Commands**:
  ```bash
  cd frontend
  npm install
  npm start
  ```
  *(Note: `npm start` runs `ng serve --proxy-config proxy.conf.json`)*

Open your web browser and navigate to **`http://localhost:4200`** to view the application!

---

## 🔗 Development Proxy Configuration

To prevent **CORS (Cross-Origin Resource Sharing)** issues during development, the Angular workspace is configured with a dev server proxy (`proxy.conf.json`):

```json
{
  "/api": {
    "target": "http://localhost:8082",
    "secure": false,
    "logLevel": "debug"
  }
}
```

Whenever the Angular application sends an HTTP request starting with `/api` (e.g. `/api/v1/centers`), the Angular dev server intercepts and forwards it to the Spring Boot server running at `http://localhost:8082`.

---

## 🛠️ Production Build (Unified Artifact)

In production, you do not need to run two separate processes. You can build the Angular application and copy the static assets into Spring Boot's static folder:

1. **Build Angular**:
   ```bash
   cd frontend
   npm run build
   ```
2. **Move Output**:
   Copy files from `frontend/dist/frontend/browser/` to `src/main/resources/static/`.
3. **Build Spring Boot Jar**:
   ```bash
   mvn clean package -DskipTests
   ```
   *(This builds a single executable `.jar` file that hosts both the frontend UI and backend REST endpoints on port 8082).*
