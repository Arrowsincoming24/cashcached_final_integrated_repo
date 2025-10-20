$baseUrl = "http://localhost:8080/fd-simulator"

Write-Host "=== TESTING SPECIFIC FAILING ENDPOINTS ===" -ForegroundColor Cyan

# Login first
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

Write-Host "`n1. Testing /api/public/test (should work)" -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/test" -Method GET
    Write-Host "[OK] $result" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n2. Testing /api/public/products (failing)" -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/products" -Method GET
    Write-Host "[OK] Found $($result.Count) products" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n3. Testing /api/public/products/active (should work)" -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/products/active" -Method GET
    Write-Host "[OK] Found $($result.Count) products" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n4. Testing /api/admin/products (should work)" -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/admin/products" -Method GET -Headers $authHeaders
    Write-Host "[OK] Found $($result.Count) products" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n5. Testing /api/admin/products/enhanced (failing)" -ForegroundColor Yellow
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
    
    $result = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/enhanced" -Method POST -Body $productBody -Headers $authHeaders
    Write-Host "[OK] Created product: $($result.productName)" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}
