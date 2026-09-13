@echo off
title Student Management System - Microservices Launcher
echo ============================================================
echo   Starting Training Center Microservices Architecture
echo ============================================================
echo.

cd /d "%~dp0"

:: Ensure Maven and Java 17 are in PATH
set "PATH=%USERPROFILE%\AppData\Local\Programs\Maven\apache-maven-3.9.9\bin;%USERPROFILE%\AppData\Local\Programs\Microsoft\jdk-17.0.10.7-hotspot\bin;%PATH%"
set "JAVA_HOME=%USERPROFILE%\AppData\Local\Programs\Microsoft\jdk-17.0.10.7-hotspot"

echo [1/6] Launching Service Registry (Eureka) on port 8761...
start "Service Registry [8761]" cmd /k "cd service-registry && mvn spring-boot:run"

echo Waiting 15 seconds for Eureka Registry to initialize...
timeout /t 15 /nobreak >nul

echo [2/6] Launching Auth Service on port 8084...
start "Auth Service [8084]" cmd /k "cd auth-service && mvn spring-boot:run"

echo [3/6] Launching Admin Service on port 8081...
start "Admin Service [8081]" cmd /k "cd admin-service && mvn spring-boot:run"

echo [4/6] Launching Trainer Service on port 8082...
start "Trainer Service [8082]" cmd /k "cd trainer-service && mvn spring-boot:run"

echo [5/6] Launching Student Service on port 8083...
start "Student Service [8083]" cmd /k "cd student-service && mvn spring-boot:run"

echo Waiting 10 seconds before starting API Gateway...
timeout /t 10 /nobreak >nul

echo [6/6] Launching API Gateway on port 8080...
start "API Gateway [8080]" cmd /k "cd api-gateway && mvn spring-boot:run"

echo.
echo ============================================================
echo   All 6 Microservices are launching in separate windows!
echo   - Service Registry: http://localhost:8761
echo   - API Gateway:      http://localhost:8080
echo   - Admin Service:    http://localhost:8081
echo   - Trainer Service:  http://localhost:8082
echo   - Student Service:  http://localhost:8083
echo   - Auth Service:     http://localhost:8084
echo.
echo   To stop all services, run 'stop-all.bat'
echo ============================================================
pause
