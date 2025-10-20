Write-Host "=== COMPREHENSIVE API ENDPOINT TESTING ===" -ForegroundColor Cyan
Write-Host "Testing all GET, POST, PUT endpoints..." -ForegroundColor Yellow
Write-Host ""

$baseUrl = "http://localhost:8080/fd-simulator"
$successCount = 0
$failCount = 0

# Helper function to test endpoint
function Test-Endpoint {
    param(
        [string]$Method,
        [string]$Url,
        [string]$Description,
        [hashtable]$Headers = @{},
        [string]$Body = $null
    )
    
    try {
        if ($Body) {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $Headers -Body $Body -ContentType "application/json"
        } else {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $Headers
        }
        Write-Host "✓ [$Method] $Description" -ForegroundColor Green
        $script:successCount++
        return $response
    } catch {
        Write-Host "✗ [$Method] $Description" -ForegroundColor Red
        Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor DarkRed
        $script:failCount++
        return $null
    }
}

Write-Host "=== AUTHENTICATION ENDPOINTS ===" -ForegroundColor Cyan

# 1. Login as Admin
$loginBody = @{
    username = "admin"
    password = "admin123"
} | ConvertTo-Json

$loginResponse = Test-Endpoint -Method "POST" -Url "$baseUrl/api/auth/login" -Description "Admin Login" -Body $loginBody

if ($loginResponse) {
    $token = $loginResponse.token
    $authHeaders = @{
        "Authorization" = "Bearer $token"
    }
    Write-Host "  Token received: $($token.Substring(0, 20))..." -ForegroundColor Gray
    Write-Host "  Role: $($loginResponse.role)" -ForegroundColor Gray
    Write-Host ""
} else {
    Write-Host "Cannot proceed without authentication token!" -ForegroundColor Red
    exit
}

Write-Host "=== ADMIN USER MANAGEMENT ENDPOINTS ===" -ForegroundColor Cyan

# 2. Get All Users
$users = Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/users" -Description "Get All Users" -Headers $authHeaders

# 3. Get All Customers
$customers = Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/users/customers" -Description "Get All Customers" -Headers $authHeaders

if ($users -and $users.Count -gt 0) {
    $userId = $users[0].id
    
    # 4. Get User by ID
    Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/users/$userId" -Description "Get User by ID ($userId)" -Headers $authHeaders
}

Write-Host ""
Write-Host "=== ADMIN PRODUCT MANAGEMENT ENDPOINTS ===" -ForegroundColor Cyan

# 5. Get All Products
$products = Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products" -Description "Get All Products" -Headers $authHeaders

# 6. Get Active Products
Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products/active" -Description "Get Active Products" -Headers $authHeaders

# 7. Get Products Summary
Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products/summary" -Description "Get Products Summary" -Headers $authHeaders

# 8. Create New Product
$newProduct = @{
    productCode = "TEST_FD_$(Get-Random -Maximum 9999)"
    productName = "Test Fixed Deposit"
    description = "Test product for API testing"
    currency = "INR"
    status = "ACTIVE"
    minAmount = 10000
    maxAmount = 1000000
    minTenureMonths = 6
    maxTenureMonths = 60
    interestRate = 7.5
    isActive = $true
} | ConvertTo-Json

$createdProduct = Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/products/enhanced" -Description "Create New Product" -Headers $authHeaders -Body $newProduct

if ($createdProduct) {
    $productId = $createdProduct.id
    Write-Host "  Created Product ID: $productId" -ForegroundColor Gray
    
    # 9. Get Product by ID
    Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products/$productId" -Description "Get Product by ID ($productId)" -Headers $authHeaders
    
    # 10. Get Product Details
    Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products/$productId/details" -Description "Get Product Details" -Headers $authHeaders
    
    # 11. Update Product
    $updateProduct = @{
        productName = "Updated Test Fixed Deposit"
        description = "Updated description"
        interestRate = 8.0
    } | ConvertTo-Json
    
    Test-Endpoint -Method "PUT" -Url "$baseUrl/api/admin/products/$productId/enhanced" -Description "Update Product" -Headers $authHeaders -Body $updateProduct
    
    # 12. Add Interest Rate
    $newRate = @{
        customerType = "RETAIL"
        termFromDays = 180
        termToDays = 365
        interestRate = 7.5
        effectiveFrom = "2025-01-01"
    } | ConvertTo-Json
    
    $createdRate = Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/products/$productId/rates" -Description "Add Interest Rate" -Headers $authHeaders -Body $newRate
    
    # 13. Get Product Rates
    Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products/$productId/rates" -Description "Get Product Rates" -Headers $authHeaders
    
    # 14. Add Term Profile
    $termProfile = @{
        termName = "6 Months"
        termMonths = 6
        minAmount = 10000
        maxAmount = 500000
        baseInterestRate = 7.0
        isActive = $true
    } | ConvertTo-Json
    
    Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/products/$productId/term-profiles" -Description "Add Term Profile" -Headers $authHeaders -Body $termProfile
    
    # 15. Get Term Profiles
    Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/products/$productId/term-profiles" -Description "Get Term Profiles" -Headers $authHeaders
}

Write-Host ""
Write-Host "=== ADMIN FIXED DEPOSIT ENDPOINTS ===" -ForegroundColor Cyan

# 16. Get All Fixed Deposits
$fds = Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/fixed-deposits" -Description "Get All Fixed Deposits" -Headers $authHeaders

# 17. Get FDs by Status
Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/fixed-deposits/status/ACTIVE" -Description "Get Active Fixed Deposits" -Headers $authHeaders

if ($fds -and $fds.Count -gt 0) {
    $fdId = $fds[0].id
    
    # 18. Get FD by ID
    Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/fixed-deposits/$fdId" -Description "Get FD by ID ($fdId)" -Headers $authHeaders
}

Write-Host ""
Write-Host "=== ADMIN DASHBOARD AND REPORTS ===" -ForegroundColor Cyan

# 19. Get Dashboard Stats
$stats = Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/dashboard/stats" -Description "Get Dashboard Statistics" -Headers $authHeaders

if ($stats) {
    Write-Host "  Total Users: $($stats.totalUsers)" -ForegroundColor Gray
    Write-Host "  Total FDs: $($stats.totalFixedDeposits)" -ForegroundColor Gray
    Write-Host "  Active FDs: $($stats.activeFixedDeposits)" -ForegroundColor Gray
}

# 20. Get Audit Logs
Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/audit-logs" -Description "Get Audit Logs" -Headers $authHeaders

# 21. Get Summary Report
Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/batch/summary-report" -Description "Get Summary Report" -Headers $authHeaders

# 22. Get FDs Nearing Maturity
Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/batch/nearing-maturity?days=30" -Description "Get FDs Nearing Maturity" -Headers $authHeaders

Write-Host ""
Write-Host "=== TIME TRAVEL FEATURES ===" -ForegroundColor Cyan

# 23. Get Time Travel Status
Test-Endpoint -Method "GET" -Url "$baseUrl/api/admin/time-travel/status" -Description "Get Time Travel Status" -Headers $authHeaders

# 24. Enable Time Travel
Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/time-travel/enable?date=2025-12-31" -Description "Enable Time Travel" -Headers $authHeaders

# 25. Fast Forward Time
Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/time-travel/fast-forward?days=30" -Description "Fast Forward 30 Days" -Headers $authHeaders

# 26. Disable Time Travel
Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/time-travel/disable" -Description "Disable Time Travel" -Headers $authHeaders

Write-Host ""
Write-Host "=== BATCH PROCESSING ===" -ForegroundColor Cyan

# 27. Process Interest
Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/batch/process-interest" -Description "Process Interest Accrual" -Headers $authHeaders

# 28. Process Matured FDs
Test-Endpoint -Method "POST" -Url "$baseUrl/api/admin/batch/process-matured" -Description "Process Matured FDs" -Headers $authHeaders

Write-Host ""
Write-Host "=== CUSTOMER ENDPOINTS ===" -ForegroundColor Cyan

# Login as Customer (if exists)
$customerLoginBody = @{
    username = "customer"
    password = "customer123"
} | ConvertTo-Json

$customerLogin = Test-Endpoint -Method "POST" -Url "$baseUrl/api/auth/login" -Description "Customer Login" -Body $customerLoginBody

if ($customerLogin) {
    $customerToken = $customerLogin.token
    $customerHeaders = @{
        "Authorization" = "Bearer $customerToken"
    }
    
    # 29. Get Customer Products
    Test-Endpoint -Method "GET" -Url "$baseUrl/api/customer/products" -Description "Get Available Products (Customer)" -Headers $customerHeaders
    
    # 30. Get Customer FDs
    Test-Endpoint -Method "GET" -Url "$baseUrl/api/customer/fixed-deposits" -Description "Get My Fixed Deposits" -Headers $customerHeaders
    
    # 31. Get Customer Dashboard Stats
    Test-Endpoint -Method "GET" -Url "$baseUrl/api/customer/dashboard/stats" -Description "Get Customer Dashboard Stats" -Headers $customerHeaders
    
    # 32. Calculate FD Returns
    $calcBody = @{
        principalAmount = 100000
        interestRate = 7.5
        tenureInMonths = 12
        compoundingFrequency = "QUARTERLY"
    } | ConvertTo-Json
    
    Test-Endpoint -Method "POST" -Url "$baseUrl/api/customer/calculate" -Description "Calculate FD Returns" -Headers $customerHeaders -Body $calcBody
    
    # 33. Create FD (if products exist)
    if ($products -and $products.Count -gt 0) {
        $fdBody = @{
            productId = $products[0].id
            principalAmount = 50000
            tenureInMonths = 12
            compoundingFrequency = "QUARTERLY"
        } | ConvertTo-Json
        
        $newFd = Test-Endpoint -Method "POST" -Url "$baseUrl/api/customer/fixed-deposits" -Description "Create New FD" -Headers $customerHeaders -Body $fdBody
    }
}

Write-Host ""
Write-Host "=== PUBLIC ENDPOINTS ===" -ForegroundColor Cyan

# 34. Get Public Products
Test-Endpoint -Method "GET" -Url "$baseUrl/api/public/products" -Description "Get Public Products"

# 35. Calculate Returns (Public)
$publicCalcBody = @{
    principalAmount = 100000
    interestRate = 7.5
    tenureInMonths = 12
    compoundingFrequency = "QUARTERLY"
} | ConvertTo-Json

Test-Endpoint -Method "POST" -Url "$baseUrl/api/public/calculate" -Description "Calculate Returns (Public)" -Body $publicCalcBody

Write-Host ""
Write-Host "=== TEST SUMMARY ===" -ForegroundColor Cyan
Write-Host "✓ Successful: $successCount" -ForegroundColor Green
Write-Host "✗ Failed: $failCount" -ForegroundColor Red
Write-Host "Total Tests: $($successCount + $failCount)" -ForegroundColor Yellow
Write-Host ""

if ($failCount -eq 0) {
    Write-Host "🎉 ALL TESTS PASSED!" -ForegroundColor Green
} else {
    Write-Host "⚠️  Some tests failed. Check the output above for details." -ForegroundColor Yellow
}
