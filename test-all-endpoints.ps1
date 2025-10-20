Write-Host "=== COMPREHENSIVE API ENDPOINT TESTING ===" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/fd-simulator"
$successCount = 0
$failCount = 0

Write-Host "=== AUTHENTICATION ENDPOINTS ===" -ForegroundColor Cyan

# 1. Login as Admin
try {
    $loginBody = '{"username":"admin","password":"admin123"}'
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
    Write-Host "[OK] POST /api/auth/login - Admin Login" -ForegroundColor Green
    $token = $loginResponse.token
    $authHeaders = @{"Authorization" = "Bearer $token"}
    Write-Host "  Token: $($token.Substring(0, 20))..." -ForegroundColor Gray
    Write-Host "  Role: $($loginResponse.role)" -ForegroundColor Gray
    $successCount++
} catch {
    Write-Host "[FAIL] POST /api/auth/login - Admin Login" -ForegroundColor Red
    Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor DarkRed
    $failCount++
    exit
}

Write-Host ""
Write-Host "=== ADMIN USER MANAGEMENT ENDPOINTS ===" -ForegroundColor Cyan

# 2. Get All Users
try {
    $users = Invoke-RestMethod -Uri "$baseUrl/api/admin/users" -Method GET -Headers $authHeaders
    Write-Host "[OK] GET /api/admin/users - Found $($users.Count) users" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/admin/users" -ForegroundColor Red
    $failCount++
}

# 3. Get All Customers
try {
    $customers = Invoke-RestMethod -Uri "$baseUrl/api/admin/users/customers" -Method GET -Headers $authHeaders
    Write-Host "[OK] GET /api/admin/users/customers - Found $($customers.Count) customers" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/admin/users/customers" -ForegroundColor Red
    $failCount++
}

# 4. Get User by ID
if ($users -and $users.Count -gt 0) {
    try {
        $userId = $users[0].id
        $user = Invoke-RestMethod -Uri "$baseUrl/api/admin/users/$userId" -Method GET -Headers $authHeaders
        Write-Host "[OK] GET /api/admin/users/$userId - User: $($user.username)" -ForegroundColor Green
        $successCount++
    } catch {
        Write-Host "[FAIL] GET /api/admin/users/{id}" -ForegroundColor Red
        $failCount++
    }
}

Write-Host ""
Write-Host "=== ADMIN PRODUCT MANAGEMENT ENDPOINTS ===" -ForegroundColor Cyan

# 5. Get All Products
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/admin/products" -Method GET -Headers $authHeaders
    Write-Host "[OK] GET /api/admin/products - Found $($products.Count) products" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/admin/products" -ForegroundColor Red
    $failCount++
}

# 6. Get Active Products
try {
    $activeProducts = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/active" -Method GET -Headers $authHeaders
    Write-Host "[OK] GET /api/admin/products/active - Found $($activeProducts.Count) active products" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/admin/products/active" -ForegroundColor Red
    $failCount++
}

# 7. Get Products Summary
try {
    $summary = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/summary" -Method GET -Headers $authHeaders
    Write-Host "[OK] GET /api/admin/products/summary" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/admin/products/summary" -ForegroundColor Red
    $failCount++
}

# 8. Create New Product
try {
    $randomCode = Get-Random -Maximum 9999
    $newProductBody = "{`"productCode`":`"TEST_FD_$randomCode`",`"productName`":`"Test Fixed Deposit`",`"description`":`"Test product`",`"currency`":`"INR`",`"status`":`"ACTIVE`",`"minAmount`":10000,`"maxAmount`":1000000,`"minTenureMonths`":6,`"maxTenureMonths`":60,`"interestRate`":7.5,`"isActive`":true}"
    $createdProduct = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/enhanced" -Method POST -Headers $authHeaders -Body $newProductBody -ContentType "application/json"
    Write-Host "[OK] POST /api/admin/products/enhanced - Created product ID: $($createdProduct.id)" -ForegroundColor Green
    $productId = $createdProduct.id
    $successCount++
} catch {
    Write-Host "[FAIL] POST /api/admin/products/enhanced" -ForegroundColor Red
    Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor DarkRed
    $failCount++
}

# 9. Get Product by ID
if ($productId) {
    try {
        $product = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/$productId" -Method GET -Headers $authHeaders
        Write-Host "[OK] GET /api/admin/products/$productId" -ForegroundColor Green
        $successCount++
    } catch {
        Write-Host "[FAIL] GET /api/admin/products/{id}" -ForegroundColor Red
        $failCount++
    }
    
    # 10. Update Product
    try {
        $updateBody = "{`"productName`":`"Updated Test FD`",`"description`":`"Updated description`",`"interestRate`":8.0}"
        $updated = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/$productId/enhanced" -Method PUT -Headers $authHeaders -Body $updateBody -ContentType "application/json"
        Write-Host "[OK] PUT /api/admin/products/$productId/enhanced - Product updated" -ForegroundColor Green
        $successCount++
    } catch {
        Write-Host "[FAIL] PUT /api/admin/products/{id}/enhanced" -ForegroundColor Red
        $failCount++
    }
    
    # 11. Add Interest Rate
    try {
        $rateBody = "{`"customerType`":`"RETAIL`",`"termFromDays`":180,`"termToDays`":365,`"interestRate`":7.5,`"effectiveFrom`":`"2025-01-01`"}"
        $rate = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/$productId/rates" -Method POST -Headers $authHeaders -Body $rateBody -ContentType "application/json"
        Write-Host "[OK] POST /api/admin/products/$productId/rates - Rate added" -ForegroundColor Green
        $successCount++
    } catch {
        Write-Host "[FAIL] POST /api/admin/products/{id}/rates" -ForegroundColor Red
        $failCount++
    }
    
    # 12. Get Product Rates
    try {
        $rates = Invoke-RestMethod -Uri "$baseUrl/api/admin/products/$productId/rates" -Method GET -Headers $authHeaders
        Write-Host "[OK] GET /api/admin/products/$productId/rates" -ForegroundColor Green
        $successCount++
    } catch {
        Write-Host "[FAIL] GET /api/admin/products/{id}/rates" -ForegroundColor Red
        $failCount++
    }
}

Write-Host ""
Write-Host "=== ADMIN FIXED DEPOSIT ENDPOINTS ===" -ForegroundColor Cyan

# 13. Get All Fixed Deposits
try {
    $fds = Invoke-RestMethod -Uri "$baseUrl/api/admin/fixed-deposits" -Method GET -Headers $authHeaders
    Write-Host "[OK] GET /api/admin/fixed-deposits - Found $($fds.Count) FDs" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/admin/fixed-deposits" -ForegroundColor Red
    $failCount++
}

# 14. Get Active FDs
try {
    $activeFds = Invoke-RestMethod -Uri "$baseUrl/api/admin/fixed-deposits/status/ACTIVE" -Method GET -Headers $authHeaders
    Write-Host "[OK] GET /api/admin/fixed-deposits/status/ACTIVE - Found $($activeFds.Count) active FDs" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/admin/fixed-deposits/status/ACTIVE" -ForegroundColor Red
    $failCount++
}

Write-Host ""
Write-Host "=== ADMIN DASHBOARD AND REPORTS ===" -ForegroundColor Cyan

# 15. Get Dashboard Stats
try {
    $stats = Invoke-RestMethod -Uri "$baseUrl/api/admin/dashboard/stats" -Method GET -Headers $authHeaders
    Write-Host "[OK] GET /api/admin/dashboard/stats" -ForegroundColor Green
    Write-Host "  Total Users: $($stats.totalUsers)" -ForegroundColor Gray
    Write-Host "  Total FDs: $($stats.totalFixedDeposits)" -ForegroundColor Gray
    Write-Host "  Active FDs: $($stats.activeFixedDeposits)" -ForegroundColor Gray
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/admin/dashboard/stats" -ForegroundColor Red
    $failCount++
}

# 16. Get Summary Report
try {
    $report = Invoke-RestMethod -Uri "$baseUrl/api/admin/batch/summary-report" -Method GET -Headers $authHeaders
    Write-Host "[OK] GET /api/admin/batch/summary-report" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/admin/batch/summary-report" -ForegroundColor Red
    $failCount++
}

# 17. Get FDs Nearing Maturity
try {
    $nearing = Invoke-RestMethod -Uri "$baseUrl/api/admin/batch/nearing-maturity?days=30" -Method GET -Headers $authHeaders
    Write-Host "[OK] GET /api/admin/batch/nearing-maturity?days=30" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/admin/batch/nearing-maturity" -ForegroundColor Red
    $failCount++
}

Write-Host ""
Write-Host "=== TIME TRAVEL FEATURES ===" -ForegroundColor Cyan

# 18. Get Time Travel Status
try {
    $ttStatus = Invoke-RestMethod -Uri "$baseUrl/api/admin/time-travel/status" -Method GET -Headers $authHeaders
    Write-Host "[OK] GET /api/admin/time-travel/status" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/admin/time-travel/status" -ForegroundColor Red
    $failCount++
}

# 19. Enable Time Travel
try {
    $tt = Invoke-RestMethod -Uri "$baseUrl/api/admin/time-travel/enable?date=2025-12-31" -Method POST -Headers $authHeaders
    Write-Host "[OK] POST /api/admin/time-travel/enable" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] POST /api/admin/time-travel/enable" -ForegroundColor Red
    $failCount++
}

# 20. Fast Forward Time
try {
    $ff = Invoke-RestMethod -Uri "$baseUrl/api/admin/time-travel/fast-forward?days=30" -Method POST -Headers $authHeaders
    Write-Host "[OK] POST /api/admin/time-travel/fast-forward" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] POST /api/admin/time-travel/fast-forward" -ForegroundColor Red
    $failCount++
}

# 21. Disable Time Travel
try {
    $dt = Invoke-RestMethod -Uri "$baseUrl/api/admin/time-travel/disable" -Method POST -Headers $authHeaders
    Write-Host "[OK] POST /api/admin/time-travel/disable" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] POST /api/admin/time-travel/disable" -ForegroundColor Red
    $failCount++
}

Write-Host ""
Write-Host "=== BATCH PROCESSING ===" -ForegroundColor Cyan

# 22. Process Interest
try {
    $pi = Invoke-RestMethod -Uri "$baseUrl/api/admin/batch/process-interest" -Method POST -Headers $authHeaders
    Write-Host "[OK] POST /api/admin/batch/process-interest" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] POST /api/admin/batch/process-interest" -ForegroundColor Red
    $failCount++
}

# 23. Process Matured FDs
try {
    $pm = Invoke-RestMethod -Uri "$baseUrl/api/admin/batch/process-matured" -Method POST -Headers $authHeaders
    Write-Host "[OK] POST /api/admin/batch/process-matured" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] POST /api/admin/batch/process-matured" -ForegroundColor Red
    $failCount++
}

Write-Host ""
Write-Host "=== PUBLIC ENDPOINTS ===" -ForegroundColor Cyan

# 24. Get Public Products
try {
    $publicProducts = Invoke-RestMethod -Uri "$baseUrl/api/public/products" -Method GET
    Write-Host "[OK] GET /api/public/products - Found $($publicProducts.Count) products" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] GET /api/public/products" -ForegroundColor Red
    $failCount++
}

# 25. Calculate Returns (Public)
try {
    $calcBody = "{`"principalAmount`":100000,`"interestRate`":7.5,`"tenureInMonths`":12,`"compoundingFrequency`":`"QUARTERLY`"}"
    $calc = Invoke-RestMethod -Uri "$baseUrl/api/public/calculate" -Method POST -Body $calcBody -ContentType "application/json"
    Write-Host "[OK] POST /api/public/calculate - Maturity Amount: $($calc.maturityAmount)" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "[FAIL] POST /api/public/calculate" -ForegroundColor Red
    $failCount++
}

Write-Host ""
Write-Host "=== TEST SUMMARY ===" -ForegroundColor Cyan
Write-Host "Success: $successCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor Red
Write-Host "Total Tests: $($successCount + $failCount)" -ForegroundColor Yellow
Write-Host ""

if ($failCount -eq 0) {
    Write-Host "ALL TESTS PASSED!" -ForegroundColor Green
} else {
    Write-Host "Some tests failed. Check the output above for details." -ForegroundColor Yellow
}
