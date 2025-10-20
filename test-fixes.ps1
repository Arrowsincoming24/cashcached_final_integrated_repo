# Test the fixed endpoints
$baseUrl = "http://localhost:8080/fd-simulator"

Write-Host "=== TESTING FIXED ENDPOINTS ===" -ForegroundColor Cyan
Write-Host ""

# Login first
Write-Host "Logging in as admin..." -ForegroundColor Yellow
try {
    $loginBody = @{
        username = "admin"
        password = "admin123"
    } | ConvertTo-Json
    
    $login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
    $token = $login.token
    Write-Host "[OK] Login successful" -ForegroundColor Green
    Write-Host ""
} catch {
    Write-Host "[FAIL] Login failed: $($_.Exception.Message)" -ForegroundColor Red
    exit
}

$authHeaders = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

# Test 1: GET /api/public/products
Write-Host "Test 1: GET /api/public/products" -ForegroundColor Yellow
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/public/products" -Method GET
    Write-Host "[OK] Public products endpoint working - Found $($products.Count) products" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 2: POST /api/public/calculate
Write-Host "Test 2: POST /api/public/calculate" -ForegroundColor Yellow
try {
    $calcBody = @{
        principalAmount = 100000
        interestRate = 7.5
        tenureInMonths = 12
        currency = "INR"
    } | ConvertTo-Json
    
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/calculate" -Method POST -Body $calcBody -ContentType "application/json"
    Write-Host "[OK] Public calculate endpoint working" -ForegroundColor Green
    Write-Host "  Principal: $($result.principalAmount)" -ForegroundColor Gray
    Write-Host "  Interest: $($result.interestAmount)" -ForegroundColor Gray
    Write-Host "  Maturity: $($result.maturityAmount)" -ForegroundColor Gray
} catch {
    Write-Host "[FAIL] Error: $($_.Exception.Message)" -ForegroundColor Red
}
Write-Host ""

# Test 3: POST /api/admin/products/enhanced
Write-Host "Test 3: POST /api/admin/products/enhanced" -ForegroundColor Yellow
try {
    $productBody = @{
        productCode = "TEST_FD_$(Get-Random -Maximum 9999)"
        productName = "Test Enhanced FD"
        description = "Test product for enhanced endpoint"
        currency = "INR"
        status = "ACTIVE"
        minAmount = 10000
        maxAmount = 1000000
        minTenureMonths = 6
        maxTenureMonths = 60
        interestRate = 7.5
        isActive = $true
    } | ConvertTo-Json
    
    $newProduct = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/enhanced" -Method POST -Body $productBody -Headers $authHeaders
    Write-Host "[OK] Enhanced product creation working - Product ID: $($newProduct.id)" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] Error: $($_.Exception.Message)" -ForegroundColor Red
    Write-Host "  Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor DarkRed
}
Write-Host ""

Write-Host "=== TEST COMPLETE ===" -ForegroundColor Cyan
