# Comprehensive Endpoint Testing Script
$baseUrl = "http://localhost:8080/fd-simulator"
$passCount = 0
$failCount = 0

function Test-Endpoint {
    param(
        [string]$Method,
        [string]$Url,
        [string]$Description,
        [hashtable]$Headers = @{},
        [string]$Body = $null
    )
    
    Write-Host "`n[$Method] $Description" -ForegroundColor Cyan
    Write-Host "URL: $Url" -ForegroundColor Gray
    
    try {
        if ($Body) {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $Headers -Body $Body -ContentType "application/json" -ErrorAction Stop
        } else {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $Headers -ErrorAction Stop
        }
        Write-Host "[✓ PASS]" -ForegroundColor Green
        $script:passCount++
        return $response
    } catch {
        Write-Host "[✗ FAIL] $($_.Exception.Message)" -ForegroundColor Red
        if ($_.Exception.Response) {
            Write-Host "  Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor DarkRed
        }
        $script:failCount++
        return $null
    }
}

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  COMPREHENSIVE ENDPOINT TEST" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# ============ AUTHENTICATION ============
Write-Host "`n========== AUTHENTICATION ==========" -ForegroundColor Yellow

$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$loginResponse = Test-Endpoint -Method "POST" -Url "$baseUrl/api/auth/login" -Description "Admin Login" -Body $loginBody

if (-not $loginResponse) {
    Write-Host "`n❌ Cannot proceed without login!" -ForegroundColor Red
    exit 1
}

$token = $loginResponse.token
$authHeaders = @{
    "Authorization" = "Bearer $token"
    "Content-Type" = "application/json"
}

Write-Host "Token: $($token.Substring(0, 30))..." -ForegroundColor Gray

# ============ PUBLIC ENDPOINTS ============
Write-Host "`n========== PUBLIC ENDPOINTS ==========" -ForegroundColor Yellow

Test-Endpoint -Method "GET" -Url "$baseUrl/api/public/test" -Description "Public Test Endpoint"

Test-Endpoint -Method "GET" -Url "$baseUrl/api/public/products" -Description "Get All Public Products"

Test-Endpoint -Method "GET" -Url "$baseUrl/api/public/products/active" -Description "Get Active Public Products"

Test-Endpoint -Method "GET" -Url "$baseUrl/api/public/products/1" -Description "Get Public Product by ID"

$calcBody = @{
    principalAmount = 100000
    interestRate = 7.5
    tenureInMonths = 12
    currency = "INR"
} | ConvertTo-Json

Test-Endpoint -Method "POST" -Url "$baseUrl/api/public/calculate" -Description "Calculate FD (Public)" -Body $calcBody

# ============ ADMIN USER ENDPOINTS ============
Write-Host "`n========== ADMIN USER ENDPOINTS ==========" -ForegroundColor Yellow

Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/users" -Description "Get All Users" -Headers $authHeaders

Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/users/customers" -Description "Get All Customers" -Headers $authHeaders

Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/users/1" -Description "Get User by ID" -Headers $authHeaders

# ============ ADMIN PRODUCT ENDPOINTS ============
Write-Host "`n========== ADMIN PRODUCT ENDPOINTS ==========" -ForegroundColor Yellow

$products = Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products" -Description "Get All Products" -Headers $authHeaders

Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products/active" -Description "Get Active Products" -Headers $authHeaders

Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products/summary" -Description "Get Products Summary" -Headers $authHeaders

if ($products -and $products.Count -gt 0) {
    $productId = $products[0].id
    Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products/$productId" -Description "Get Product by ID" -Headers $authHeaders
    
    Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products/$productId/details" -Description "Get Product Details" -Headers $authHeaders
}

# Test creating product with basic endpoint
$newProductBasic = @{
    productCode = "TEST_BASIC_$(Get-Random -Maximum 9999)"
    productName = "Test Basic Product"
    description = "Basic product creation test"
    currency = "INR"
    minAmount = 10000
    maxAmount = 1000000
    minTenureMonths = 6
    maxTenureMonths = 60
    interestRate = 7.5
    isActive = $true
} | ConvertTo-Json

$createdBasic = Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/products" -Description "Create Product (Basic)" -Headers $authHeaders -Body $newProductBasic

# Test creating product with enhanced endpoint
$newProductEnhanced = @{
    productCode = "TEST_ENH_$(Get-Random -Maximum 9999)"
    productName = "Test Enhanced Product"
    description = "Enhanced product creation test"
    currency = "INR"
    status = "ACTIVE"
    minAmount = 10000
    maxAmount = 1000000
    minTenureMonths = 6
    maxTenureMonths = 60
    interestRate = 7.5
    isActive = $true
} | ConvertTo-Json

$createdEnhanced = Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/products/enhanced" -Description "Create Product (Enhanced)" -Headers $authHeaders -Body $newProductEnhanced

# ============ ADMIN FIXED DEPOSIT ENDPOINTS ============
Write-Host "`n========== ADMIN FD ENDPOINTS ==========" -ForegroundColor Yellow

Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/fixed-deposits" -Description "Get All Fixed Deposits" -Headers $authHeaders

Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/fixed-deposits/status/ACTIVE" -Description "Get Active FDs" -Headers $authHeaders

# ============ ADMIN DASHBOARD ============
Write-Host "`n========== ADMIN DASHBOARD ==========" -ForegroundColor Yellow

Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/dashboard/stats" -Description "Get Dashboard Stats" -Headers $authHeaders

Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/batch/summary-report" -Description "Get Summary Report" -Headers $authHeaders

Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/batch/nearing-maturity?days=30" -Description "Get FDs Nearing Maturity" -Headers $authHeaders

# ============ TIME TRAVEL ============
Write-Host "`n========== TIME TRAVEL ENDPOINTS ==========" -ForegroundColor Yellow

Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/time-travel/status" -Description "Get Time Travel Status" -Headers $authHeaders

Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/time-travel/enable?date=2025-12-31" -Description "Enable Time Travel" -Headers $authHeaders

Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/time-travel/fast-forward?days=30" -Description "Fast Forward Time" -Headers $authHeaders

Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/time-travel/disable" -Description "Disable Time Travel" -Headers $authHeaders

# ============ BATCH PROCESSING ============
Write-Host "`n========== BATCH PROCESSING ==========" -ForegroundColor Yellow

Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/batch/process-interest" -Description "Process Interest" -Headers $authHeaders

Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/batch/process-matured" -Description "Process Matured FDs" -Headers $authHeaders

# ============ SUMMARY ============
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  TEST SUMMARY" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "✓ PASSED: $passCount" -ForegroundColor Green
Write-Host "✗ FAILED: $failCount" -ForegroundColor Red
Write-Host "TOTAL: $($passCount + $failCount)" -ForegroundColor Yellow

$successRate = [math]::Round(($passCount / ($passCount + $failCount)) * 100, 2)
Write-Host "Success Rate: $successRate%" -ForegroundColor $(if ($successRate -ge 90) { "Green" } elseif ($successRate -ge 70) { "Yellow" } else { "Red" })

if ($failCount -eq 0) {
    Write-Host "`n🎉 ALL TESTS PASSED!" -ForegroundColor Green
} else {
    Write-Host "`n⚠️  Some tests failed. Review the output above." -ForegroundColor Yellow
}
