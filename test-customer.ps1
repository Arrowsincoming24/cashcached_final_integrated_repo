$baseUrl = "http://localhost:8080/fd-simulator"

$loginBody = '{"username":"admin","password":"admin123"}'
$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $login.token
$headers = @{"Authorization" = "Bearer $token"}

Write-Host "Testing customer endpoints..." -ForegroundColor Cyan

Write-Host "`n1. /api/customer/products..." -NoNewline
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/customer/products" -Method GET -Headers $headers
    Write-Host " OK - $($products.Count) products" -ForegroundColor Green
} catch {
    Write-Host " FAIL" -ForegroundColor Red
    Write-Host "   Status: $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n2. /api/customer/fixed-deposits..." -NoNewline
try {
    $fds = Invoke-RestMethod -Uri "$baseUrl/api/customer/fixed-deposits" -Method GET -Headers $headers
    Write-Host " OK - $($fds.Count) FDs" -ForegroundColor Green
} catch {
    Write-Host " FAIL" -ForegroundColor Red
}

Write-Host "`n3. /api/customer/dashboard/stats..." -NoNewline
try {
    $stats = Invoke-RestMethod -Uri "$baseUrl/api/customer/dashboard/stats" -Method GET -Headers $headers
    Write-Host " OK" -ForegroundColor Green
} catch {
    Write-Host " FAIL" -ForegroundColor Red
}
