$baseUrl = "http://localhost:8080/fd-simulator"

Write-Host "=== DEBUGGING ENDPOINT ISSUES ===" -ForegroundColor Cyan
Write-Host ""

# Test various product endpoints
Write-Host "Test 1: /api/public/products/active" -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/products/active" -Method GET
    Write-Host "[OK] Found $($result.Count) products" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest 2: /api/public/products" -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/products" -Method GET
    Write-Host "[OK] Found $($result.Count) products" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest 3: /api/public/products/1" -ForegroundColor Yellow
try {
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/products/1" -Method GET
    Write-Host "[OK] Product: $($result.productName)" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`nTest 4: /api/public/calculate" -ForegroundColor Yellow
try {
    $body = @{
        principalAmount = 50000
        interestRate = 7.0
        tenureInMonths = 12
        currency = "INR"
    } | ConvertTo-Json
    
    $result = Invoke-RestMethod -Uri "$baseUrl/api/public/calculate" -Method POST -Body $body -ContentType "application/json"
    Write-Host "[OK] Maturity: $($result.maturityAmount)" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Message)" -ForegroundColor Red
}
