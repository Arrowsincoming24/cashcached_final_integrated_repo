# Quick endpoint test
$baseUrl = "http://localhost:8080/fd-simulator"

Write-Host "Testing endpoints..." -ForegroundColor Cyan

# Login first
try {
    $loginBody = '{"username":"admin","password":"admin123"}'
    $login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
    $token = $login.token
    Write-Host "[OK] Login successful" -ForegroundColor Green
    Write-Host "Token: $($token.Substring(0,30))..." -ForegroundColor Gray
} catch {
    Write-Host "[FAIL] Login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit
}

$headers = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

# Test users endpoint
Write-Host "`nTesting /api/admin/users..." -ForegroundColor Yellow
try {
    $users = Invoke-RestMethod -Uri "$baseUrl/api/admin/users" -Method GET -Headers $headers
    Write-Host "[OK] Users: $($users.Count)" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] Error: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    Write-Host "  Message: $($_.Exception.Message)" -ForegroundColor DarkRed
}

# Test products endpoint  
Write-Host "`nTesting /api/admin/products..." -ForegroundColor Yellow
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/admin/products" -Method GET -Headers $headers
    Write-Host "[OK] Products: $($products.Count)" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] Error: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    Write-Host "  Message: $($_.Exception.Message)" -ForegroundColor DarkRed
}

# Test dashboard
Write-Host "`nTesting /api/admin/dashboard/stats..." -ForegroundColor Yellow
try {
    $stats = Invoke-RestMethod -Uri "$baseUrl/api/admin/dashboard/stats" -Method GET -Headers $headers
    Write-Host "[OK] Dashboard stats retrieved" -ForegroundColor Green
    Write-Host "  Total Users: $($stats.totalUsers)" -ForegroundColor Gray
} catch {
    Write-Host "[FAIL] Error: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

# Test FDs
Write-Host "`nTesting /api/admin/fixed-deposits..." -ForegroundColor Yellow
try {
    $fds = Invoke-RestMethod -Uri "$baseUrl/api/admin/fixed-deposits" -Method GET -Headers $headers
    Write-Host "[OK] Fixed Deposits: $($fds.Count)" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] Error: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

Write-Host "`nTest complete!" -ForegroundColor Cyan
