# Command Line Reference (Windows-friendly)

This document lists core execution commands for compiling, building, testing, and running the application.

---

## 1. Backend Commands (Run from Project Root)

### Run Spring Boot App in Development Mode:
```powershell
mvn spring-boot:run
```

### Compile Code (Validation Only):
```powershell
mvn compile
```

### Run Backend Unit Tests:
```powershell
mvn test
```

### Package Application to Runnable Jar (Excludes Tests):
```powershell
mvn package -DskipTests
```

---

## 2. Frontend Commands (Run from `frontend/` Directory)

Change directory first:
```powershell
cd frontend
```

### Run Angular Dev Server (Port 4200):
```powershell
npm start
```
*(Executes `ng serve --proxy-config proxy.conf.json`)*

### Run Production Build Compilation:
```powershell
npm run build
```

---

## 3. Production Running (Run from Project Root)

After packaging the application, start the self-contained backend JAR (which hosts the Angular frontend as well on port 8082):
```powershell
java -jar target\backend-0.0.1-SNAPSHOT.jar
```
