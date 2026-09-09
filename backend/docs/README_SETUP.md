# 🎓 Student Management System - Project Guide

This project is organized into two clean, independent root workspaces under **`Student-Management-System/`**:

---

## 📁 Workspace Directory Structure

```text
Student-Management-System/
├── frontend/                     <-- ALL FRONTEND CODE & ASSETS
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/       <-- Angular UI Pages (Dashboard, Centers, Trainers, Students, etc.)
│   │   │   ├── services/         <-- REST API Service Layer
│   │   │   └── app.routes.ts     <-- Client Navigation Routes
│   │   ├── index.html
│   │   └── main.ts
│   ├── proxy.conf.json           <-- Local Dev Proxy (forwards /api to http://localhost:8082)
│   ├── angular.json              <-- Angular Workspace settings
│   └── package.json              <-- Frontend Dependencies & Scripts
│
└── backend/                      <-- ALL BACKEND CODE, DOCS & DATABASE
    ├── src/
    │   ├── main/java/            <-- Spring Boot REST API (Controllers, Services, Repositories, Security)
    │   └── main/resources/       <-- application.properties (MySQL Configuration)
    ├── docs/                     <-- All Project Documentation & Architecture Guides
    ├── credentials.html          <-- Seeded Logins Reference Page
    ├── docker-compose.yml        <-- Container configuration
    ├── Dockerfile                <-- Backend container build
    └── pom.xml                   <-- Maven Project Dependencies & Plugins
```

---

## 🚀 How to Run the Application

### 1. Backend (Spring Boot API)
Runs independently on port **8082**:
```bash
cd backend
mvn spring-boot:run
```
- **API URL**: `http://localhost:8082`
- **Swagger Documentation**: `http://localhost:8082/swagger-ui/index.html`

### 2. Frontend (Angular Application)
Runs independently on port **4200**:
```bash
cd frontend
npm start
```
- **Frontend App**: `http://localhost:4200`

---

## 🔑 Login Credentials
View [credentials.html](file:///d:/antigravity/Student-Management-System/backend/credentials.html) in your browser:
- **Admin**: `admin` / `adminpassword`
- **Trainer**: `rajesh_patil_pun` / `trainerpassword`
- **Student**: `arjun_kulkarni_pun_1` / `studentpassword`
