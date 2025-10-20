$baseUrl = "http://localhost:8080/fd-simulator"

Write-Host "=== DEBUGGING SERVICE INJECTION ===" -ForegroundColor Cyan

Write-Host "`n1. Testing /api/public/test (simple endpoint)" -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/test" -Method GET
    Write-Host "[OK] $result" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n2. Testing /api/admin/products (working endpoint)" -ForegroundColor Yellow
try {
    $loginBody = @{
        username = "admin"
        password = "admin123"
    } | ConvertTo-Json
    
    $login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
    $token = $login.token
    $authHeaders = @{
        "Authorization" = "Bearer $token"
    }
    
    $result = Invoke-RestMethod -Uri "$baseUrl/api/admin/products" -Method GET -Headers $authHeaders
    Write-Host "[OK] Admin products works - Found $($result.Count) products" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n3. Testing /api/public/products (failing endpoint)" -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/products" -Method GET
    Write-Host "[OK] Found $($result.Count) products" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "Response: $($_.Exception.Response)" -ForegroundColor DarkRed
}
