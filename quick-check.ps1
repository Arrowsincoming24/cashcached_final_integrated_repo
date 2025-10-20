# Quick CashCached Application Check
Write-Host "`n=== CASHCACHED QUICK CHECK ===" -ForegroundColor Cyan

$baseUrl = "http://localhost:8080/fd-simulator"
$passed = 0
$failed = 0

function Test-Url {
    param($url, $name)
    try {
        $response = Invoke-WebRequest -Uri $url -UseBasicParsing -TimeoutSec 5
        if ($response.StatusCode -eq 200) {
            Write-Host "[OK] $name" -ForegroundColor Green
            $script:passed++
        }
    } catch {
        Write-Host "[FAIL] $name" -ForegroundColor Red
        $script:failed++
    }
}

# Test Pages
Write-Host "`nTesting Pages..." -ForegroundColor Yellow
Test-Url "$baseUrl/" "Home Page"
Test-Url "$baseUrl/login" "Login Page"
Test-Url "$baseUrl/register" "Register Page"
Test-Url "$baseUrl/admin/dashboard" "Admin Dashboard"
Test-Url "$baseUrl/customer/dashboard" "Customer Dashboard"

# Test Developer Tools
Write-Host "`nTesting Developer Tools..." -ForegroundColor Yellow
Test-Url "$baseUrl/swagger-ui/index.html" "Swagger UI"
Test-Url "$baseUrl/h2-console" "H2 Console"

# Test API
Write-Host "`nTesting API..." -ForegroundColor Yellow
try {
    $body = @{username="admin"; password="admin123"} | ConvertTo-Json
    $response = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method Post -Body $body -ContentType "application/json"
    if ($response.token) {
        Write-Host "[OK] API Authentication" -ForegroundColor Green
        $script:passed++
    }
} catch {
    Write-Host "[FAIL] API Authentication" -ForegroundColor Red
    $script:failed++
}

# Summary
Write-Host "`n=== RESULTS ===" -ForegroundColor Cyan
Write-Host "Passed: $passed" -ForegroundColor Green
Write-Host "Failed: $failed" -ForegroundColor Red
Write-Host "Total: $($passed + $failed)" -ForegroundColor White

if ($failed -eq 0) {
    Write-Host "`n✅ ALL CHECKS PASSED! Application is running perfectly!" -ForegroundColor Green
} else {
    Write-Host "`n⚠️ Some checks failed. Please verify the application is running." -ForegroundColor Yellow
}

Write-Host "`nQuick Access:" -ForegroundColor Cyan
Write-Host "  Home:    $baseUrl/" -ForegroundColor White
Write-Host "  Swagger: $baseUrl/swagger-ui/index.html" -ForegroundColor White
Write-Host "  H2 DB:   $baseUrl/h2-console" -ForegroundColor White
Write-Host "`n"
