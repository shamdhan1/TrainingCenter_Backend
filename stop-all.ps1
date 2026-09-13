Write-Host "Stopping All Training Center Microservices (Ports 8761, 8080-8084, 4200)..." -ForegroundColor Yellow

$ports = @(8761, 8080, 8081, 8082, 8083, 8084, 4200)

foreach ($port in $ports) {
    $connections = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue | Where-Object { $_.State -eq 'Listen' }
    if ($connections) {
        foreach ($conn in $connections) {
            $pidToKill = $conn.OwningProcess
            if ($pidToKill -gt 0) {
                Write-Host "Killing process on port $port (PID: $pidToKill)..." -ForegroundColor Red
                Stop-Process -Id $pidToKill -Force -ErrorAction SilentlyContinue
            }
        }
    }
}

Write-Host "All services stopped successfully." -ForegroundColor Green
