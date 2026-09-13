Write-Host "============================================================" -ForegroundColor Cyan
Write-Host "  Starting Training Center Microservices Architecture" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Cyan

$backendDir = $PSScriptRoot
$env:PATH = "$env:USERPROFILE\AppData\Local\Programs\Maven\apache-maven-3.9.9\bin;$env:USERPROFILE\AppData\Local\Programs\Microsoft\jdk-17.0.10.7-hotspot\bin;" + $env:PATH
$env:JAVA_HOME = "$env:USERPROFILE\AppData\Local\Programs\Microsoft\jdk-17.0.10.7-hotspot"

Write-Host "[1/6] Launching Service Registry (Eureka) on port 8761..." -ForegroundColor Yellow
Start-Process cmd.exe -ArgumentList "/k cd /d `"$backendDir\service-registry`" && mvn spring-boot:run" -WorkingDirectory "$backendDir\service-registry"

Write-Host "Waiting 15 seconds for Eureka Registry to initialize..." -ForegroundColor Gray
Start-Sleep -Seconds 15

Write-Host "[2/6] Launching Auth Service on port 8084..." -ForegroundColor Yellow
Start-Process cmd.exe -ArgumentList "/k cd /d `"$backendDir\auth-service`" && mvn spring-boot:run" -WorkingDirectory "$backendDir\auth-service"

Write-Host "[3/6] Launching Admin Service on port 8081..." -ForegroundColor Yellow
Start-Process cmd.exe -ArgumentList "/k cd /d `"$backendDir\admin-service`" && mvn spring-boot:run" -WorkingDirectory "$backendDir\admin-service"

Write-Host "[4/6] Launching Trainer Service on port 8082..." -ForegroundColor Yellow
Start-Process cmd.exe -ArgumentList "/k cd /d `"$backendDir\trainer-service`" && mvn spring-boot:run" -WorkingDirectory "$backendDir\trainer-service"

Write-Host "[5/6] Launching Student Service on port 8083..." -ForegroundColor Yellow
Start-Process cmd.exe -ArgumentList "/k cd /d `"$backendDir\student-service`" && mvn spring-boot:run" -WorkingDirectory "$backendDir\student-service"

Write-Host "Waiting 10 seconds before starting API Gateway..." -ForegroundColor Gray
Start-Sleep -Seconds 10

Write-Host "[6/6] Launching API Gateway on port 8080..." -ForegroundColor Yellow
Start-Process cmd.exe -ArgumentList "/k cd /d `"$backendDir\api-gateway`" && mvn spring-boot:run" -WorkingDirectory "$backendDir\api-gateway"

Write-Host ""
Write-Host "============================================================" -ForegroundColor Green
Write-Host "  All 6 Microservices are launching in separate windows!" -ForegroundColor Green
Write-Host "  - Service Registry: http://localhost:8761" -ForegroundColor Cyan
Write-Host "  - API Gateway:      http://localhost:8080" -ForegroundColor Cyan
Write-Host "  - Admin Service:    http://localhost:8081" -ForegroundColor Cyan
Write-Host "  - Trainer Service:  http://localhost:8082" -ForegroundColor Cyan
Write-Host "  - Student Service:  http://localhost:8083" -ForegroundColor Cyan
Write-Host "  - Auth Service:     http://localhost:8084" -ForegroundColor Cyan
Write-Host "============================================================" -ForegroundColor Green
