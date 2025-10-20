$baseUrl = "http://localhost:8080/fd-simulator"

Write-Host "Testing /api/public/test..." -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/test" -Method GET
    Write-Host "[OK] $result" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}
