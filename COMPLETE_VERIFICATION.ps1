Write-Host "========================================" -ForegroundColor Cyan
Write-Host "   FD SIMULATOR - COMPLETE VERIFICATION" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080/fd-simulator"
$successCount = 0
$failCount = 0

# Test Swagger UI
Write-Host "1. Testing Swagger UI..." -ForegroundColor Yellow
try {
    $swagger = Invoke-WebRequest -Uri "$baseUrl/swagger-ui/index.html" -UseBasicParsing
    if ($swagger.StatusCode -eq 200) {
        Write-Host "   [OK] Swagger UI accessible" -ForegroundColor Green
        Write-Host "   URL: $baseUrl/swagger-ui/index.html" -ForegroundColor Gray
        $successCount++
    }
} catch {
    Write-Host "   [FAIL] Swagger UI not accessible" -ForegroundColor Red
    $failCount++
}

# Test API Documentation
Write-Host "`n2. Testing API Documentation..." -ForegroundColor Yellow
try {
    $apiDocs = Invoke-RestMethod -Uri "$baseUrl/v3/api-docs"
    Write-Host "   [OK] API docs available" -ForegroundColor Green
    Write-Host "   Title: $($apiDocs.info.title)" -ForegroundColor Gray
    Write-Host "   Version: $($apiDocs.info.version)" -ForegroundColor Gray
    $successCount++
} catch {
    Write-Host "   [FAIL] API docs not available" -ForegroundColor Red
    $failCount++
}

# Login
Write-Host "`n3. Testing Authentication..." -ForegroundColor Yellow
try {
    $loginBody = '{"username":"admin","password":"admin123"}'
    $login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
    $token = $login.token
    Write-Host "   [OK] Login successful" -ForegroundColor Green
    Write-Host "   Role: $($login.role)" -ForegroundColor Gray
    $successCount++
} catch {
    Write-Host "   [FAIL] Login failed" -ForegroundColor Red
    $failCount++
    exit
}

$headers = @{"Authorization" = "Bearer $token"}

# Test Admin Endpoints
Write-Host "`n4. Testing Admin Endpoints..." -ForegroundColor Yellow

$endpoints = @(
    @{name="Users"; url="/api/admin/users"},
    @{name="Products"; url="/api/admin/products"},
    @{name="Products Summary"; url="/api/admin/products/summary"},
    @{name="Fixed Deposits"; url="/api/admin/fixed-deposits"},
    @{name="Dashboard Stats"; url="/api/admin/dashboard/stats"},
    @{name="Audit Logs"; url="/api/admin/audit-logs"}
)

foreach ($endpoint in $endpoints) {
    try {
        $result = Invoke-RestMethod -Uri "$baseUrl$($endpoint.url)" -Method GET -Headers $headers
        Write-Host "   [OK] $($endpoint.name)" -ForegroundColor Green
        $successCount++
    } catch {
        Write-Host "   [FAIL] $($endpoint.name)" -ForegroundColor Red
        $failCount++
    }
}

# Test Time Travel
Write-Host "`n5. Testing Time Travel Features..." -ForegroundColor Yellow
try {
    $ttStatus = Invoke-RestMethod -Uri "$baseUrl/api/admin/time-travel/status" -Method GET -Headers $headers
    Write-Host "   [OK] Time Travel Status" -ForegroundColor Green
    Write-Host "   Enabled: $($ttStatus.enabled)" -ForegroundColor Gray
    $successCount++
} catch {
    Write-Host "   [FAIL] Time Travel Status" -ForegroundColor Red
    $failCount++
}

try {
    $ttEnable = Invoke-RestMethod -Uri "$baseUrl/api/admin/time-travel/enable?date=2026-01-01" -Method POST -Headers $headers
    Write-Host "   [OK] Time Travel Enable" -ForegroundColor Green
    $successCount++
    
    # Disable it again
    Invoke-RestMethod -Uri "$baseUrl/api/admin/time-travel/disable" -Method POST -Headers $headers | Out-Null
} catch {
    Write-Host "   [FAIL] Time Travel Enable" -ForegroundColor Red
    $failCount++
}

# Test Batch Processing
Write-Host "`n6. Testing Batch Processing..." -ForegroundColor Yellow
try {
    $batch = Invoke-RestMethod -Uri "$baseUrl/api/admin/batch/summary-report" -Method GET -Headers $headers
    Write-Host "   [OK] Summary Report" -ForegroundColor Green
    Write-Host "   Total FDs: $($batch.totalFds)" -ForegroundColor Gray
    $successCount++
} catch {
    Write-Host "   [FAIL] Summary Report" -ForegroundColor Red
    $failCount++
}

try {
    $matured = Invoke-RestMethod -Uri "$baseUrl/api/admin/batch/process-matured" -Method POST -Headers $headers
    Write-Host "   [OK] Process Matured FDs" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "   [FAIL] Process Matured FDs" -ForegroundColor Red
    $failCount++
}

# Test Customer Endpoints
Write-Host "`n7. Testing Customer Endpoints..." -ForegroundColor Yellow
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/customer/products" -Method GET -Headers $headers
    Write-Host "   [OK] Customer Products" -ForegroundColor Green
    $successCount++
} catch {
    Write-Host "   [FAIL] Customer Products" -ForegroundColor Red
    $failCount++
}

try {
    $calcBody = '{"principalAmount":100000,"interestRate":7.5,"tenureInMonths":12,"compoundingFrequency":"QUARTERLY"}'
    $calc = Invoke-RestMethod -Uri "$baseUrl/api/customer/calculate" -Method POST -Headers $headers -Body $calcBody -ContentType "application/json"
    Write-Host "   [OK] FD Calculator" -ForegroundColor Green
    Write-Host "   Maturity Amount: $($calc.maturityAmount)" -ForegroundColor Gray
    $successCount++
} catch {
    Write-Host "   [FAIL] FD Calculator" -ForegroundColor Red
    $failCount++
}

# Summary
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "   VERIFICATION COMPLETE" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Results:" -ForegroundColor Yellow
Write-Host "  Success: $successCount" -ForegroundColor Green
Write-Host "  Failed:  $failCount" -ForegroundColor Red
Write-Host "  Total:   $($successCount + $failCount)" -ForegroundColor White
Write-Host ""

if ($failCount -eq 0) {
    Write-Host "ALL TESTS PASSED!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Application Status: FULLY OPERATIONAL" -ForegroundColor Green
    Write-Host ""
    Write-Host "Access Points:" -ForegroundColor Cyan
    Write-Host "  Swagger UI:       $baseUrl/swagger-ui/index.html" -ForegroundColor White
    Write-Host "  Admin Dashboard:  $baseUrl/admin/dashboard" -ForegroundColor White
    Write-Host "  Customer Portal:  $baseUrl/customer/dashboard" -ForegroundColor White
    Write-Host "  H2 Console:       $baseUrl/h2-console" -ForegroundColor White
    Write-Host ""
    Write-Host "Ready for production use!" -ForegroundColor Green
} else {
    Write-Host "Some tests failed. Please review the output above." -ForegroundColor Yellow
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
