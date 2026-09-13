@echo off
title Student Management System - Stop Microservices
echo ============================================================
echo   Stopping All Training Center Microservices (Ports 8761, 8080-8084)
echo ============================================================
echo.

for %%p in (8761 8080 8081 8082 8083 8084) do (
    echo Terminating processes on port %%p...
    for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":%%p" ^| findstr "LISTENING"') do (
        taskkill /F /PID %%a 2>nul
        if not errorlevel 1 (
            echo  - Successfully terminated process on port %%p [PID: %%a]
        )
    )
)

echo.
echo ============================================================
echo   All microservice processes have been stopped.
echo ============================================================
pause
