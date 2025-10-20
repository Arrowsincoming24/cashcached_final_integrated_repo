$baseUrl = "http://localhost:8080/fd-simulator"

# Login
$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $login.token
$authHeaders = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

Write-Host "Testing alternative path: /api/admin/products-enhanced" -ForegroundColor Yellow
try {
    $productBody = @{
        productCode = "TEST_$(Get-Random -Maximum 9999)"
        productName = "Test Product"
        description = "Test"
        currency = "INR"
        status = "ACTIVE"
        minAmount = 10000
        maxAmount = 1000000
        minTenureMonths = 6
        maxTenureMonths = 60
        interestRate = 7.5
        isActive = $true
    } | ConvertTo-Json
    
    $result = Invoke-RestMethod -Uri "$baseUrl/api/admin/products-enhanced" -Method POST -Body $productBody -Headers $authHeaders
    Write-Host "[OK] Created product via /products-enhanced: $($result.productName)" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTesting /api/public/products" -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/products" -Method GET
    Write-Host "[OK] Found $($result.Count) products" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}
