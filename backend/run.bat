@echo off
title Training Center - Backend (Spring Boot)
echo ========================================================
echo Starting Training Center Backend API (Port 8082)...
echo ========================================================

:: Ensure environment variables are loaded
set "PATH=%USERPROFILE%\AppData\Local\Programs\Maven\apache-maven-3.9.9\bin;%USERPROFILE%\AppData\Local\Programs\Microsoft\jdk-17.0.10.7-hotspot\bin;%PATH%"
set "JAVA_HOME=%USERPROFILE%\AppData\Local\Programs\Microsoft\jdk-17.0.10.7-hotspot\"

cd /d "%~dp0"
mvn spring-boot:run
pause
