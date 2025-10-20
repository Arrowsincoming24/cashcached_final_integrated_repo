$baseUrl = "http://localhost:8080/fd-simulator"

# Login
$loginBody = '{"username":"admin","password":"admin123"}'
$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $login.token

$headers = @{
    "Authorization" = "Bearer $token"
}

# Test endpoints that work
Write-Host "Testing working endpoints:" -ForegroundColor Cyan
Write-Host "  /api/admin/users: " -NoNewline
try {
    $users = Invoke-RestMethod -Uri "$baseUrl/api/admin/users" -Method GET -Headers $headers
    Write-Host "OK ($($users.Count) users)" -ForegroundColor Green
} catch {
    Write-Host "FAIL" -ForegroundColor Red
}

Write-Host "  /api/admin/fixed-deposits: " -NoNewline
try {
    $fds = Invoke-RestMethod -Uri "$baseUrl/api/admin/fixed-deposits" -Method GET -Headers $headers
    Write-Host "OK ($($fds.Count) FDs)" -ForegroundColor Green
} catch {
    Write-Host "FAIL" -ForegroundColor Red
}

Write-Host "  /api/admin/dashboard/stats: " -NoNewline
try {
    $stats = Invoke-RestMethod -Uri "$baseUrl/api/admin/dashboard/stats" -Method GET -Headers $headers
    Write-Host "OK" -ForegroundColor Green
} catch {
    Write-Host "FAIL" -ForegroundColor Red
}

# Test product endpoints
Write-Host "`nTesting product endpoints:" -ForegroundColor Cyan
Write-Host "  /api/admin/products: " -NoNewline
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/admin/products" -Method GET -Headers $headers
    Write-Host "OK ($($products.Count) products)" -ForegroundColor Green
} catch {
    Write-Host "FAIL - $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

Write-Host "  /api/admin/products/active: " -NoNewline
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/active" -Method GET -Headers $headers
    Write-Host "OK ($($products.Count) products)" -ForegroundColor Green
} catch {
    Write-Host "FAIL - $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}

Write-Host "  /api/admin/products/summary: " -NoNewline
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/summary" -Method GET -Headers $headers
    Write-Host "OK ($($products.Count) products)" -ForegroundColor Green
} catch {
    Write-Host "FAIL - $($_.Exception.Response.StatusCode)" -ForegroundColor Red
}
