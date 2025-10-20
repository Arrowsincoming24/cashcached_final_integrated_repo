$baseUrl = "http://localhost:8080/fd-simulator"

$loginBody = '{"username":"admin","password":"admin123"}'
$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $login.token

$headers = @{"Authorization" = "Bearer $token"}

Write-Host "Testing /api/admin/products-list..." -NoNewline
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/admin/products-list" -Method GET -Headers $headers
    Write-Host " [OK] $result" -ForegroundColor Green
} catch {
    Write-Host " [FAIL]" -ForegroundColor Red
}

Write-Host "Testing /api/admin/products..." -NoNewline
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/admin/products" -Method GET -Headers $headers
    Write-Host " [OK] Count: $($result.Count)" -ForegroundColor Green
} catch {
    Write-Host " [FAIL]" -ForegroundColor Red
}
