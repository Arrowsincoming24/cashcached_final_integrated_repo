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

Write-Host "Testing /api/admin/create-product-enhanced" -ForegroundColor Yellow
try {
    $productBody = @{
        productCode = "TEST_$(Get-Random -Maximum 9999)"
        productName = "Test Enhanced Product"
        description = "Testing enhanced endpoint"
        currency = "INR"
        status = "ACTIVE"
        minAmount = 10000
        maxAmount = 1000000
        minTenureMonths = 6
        maxTenureMonths = 60
        interestRate = 7.5
        isActive = $true
    } | ConvertTo-Json
    
    Write-Host "Calling: POST $baseUrl/api/admin/create-product-enhanced" -ForegroundColor Gray
    $result = Invoke-RestMethod -Uri "$baseUrl/api/admin/create-product-enhanced" -Method POST -Body $productBody -Headers $authHeaders
    Write-Host "[✅ SUCCESS] Created product: $($result.productName) (ID: $($result.id))" -ForegroundColor Green
} catch {
    Write-Host "[❌ FAILED] $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        Write-Host "Status Code: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor DarkRed
    }
}
