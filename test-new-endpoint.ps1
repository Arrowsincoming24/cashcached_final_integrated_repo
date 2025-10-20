$baseUrl = "http://localhost:8080/fd-simulator"

# Login
$loginBody = '{"username":"admin","password":"admin123"}'
$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $login.token

$headers = @{
    "Authorization" = "Bearer $token"
}

# Test the new test endpoint
Write-Host "Testing /api/test/products-count..." -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/test/products-count" -Method GET -Headers $headers
    Write-Host "[OK] $result" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}

# Test the original endpoint
Write-Host "`nTesting /api/admin/products..." -ForegroundColor Yellow
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/admin/products" -Method GET -Headers $headers
    Write-Host "[OK] Count: $($products.Count)" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}
