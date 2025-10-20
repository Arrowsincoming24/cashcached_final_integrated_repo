$baseUrl = "http://localhost:8080/fd-simulator"

# Login
$loginBody = '{"username":"admin","password":"admin123"}'
$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $login.token
$headers = @{"Authorization" = "Bearer $token"}

Write-Host "Testing product endpoints..." -ForegroundColor Cyan

Write-Host "`n1. /api/admin/products..." -NoNewline
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/admin/products" -Method GET -Headers $headers
    Write-Host " OK - $($products.Count) products" -ForegroundColor Green
    if ($products.Count -gt 0) {
        Write-Host "   First product: $($products[0].productName)" -ForegroundColor Gray
    }
} catch {
    Write-Host " FAIL - $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n2. /api/admin/products/summary..." -NoNewline
try {
    $summary = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/summary" -Method GET -Headers $headers
    Write-Host " OK - $($summary.Count) products" -ForegroundColor Green
    if ($summary.Count -gt 0) {
        Write-Host "   First product: $($summary[0].productName)" -ForegroundColor Gray
    }
} catch {
    Write-Host " FAIL - $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "   Response: $($_.Exception.Response.StatusCode)" -ForegroundColor DarkRed
}

Write-Host "`n3. /api/admin/products/active..." -NoNewline
try {
    $active = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/active" -Method GET -Headers $headers
    Write-Host " OK - $($active.Count) products" -ForegroundColor Green
} catch {
    Write-Host " FAIL - $($_.Exception.Message)" -ForegroundColor Red
}
