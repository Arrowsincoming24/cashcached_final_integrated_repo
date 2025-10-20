$baseUrl = "http://localhost:8080/fd-simulator"

# Login
$loginBody = '{"username":"admin","password":"admin123"}'
$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $login.token
Write-Host "Token received" -ForegroundColor Green

$headers = @{
    "Authorization" = "Bearer $token"
}

# Try accessing products with verbose error
Write-Host "`nTesting /api/admin/products..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/api/admin/products" -Method GET -Headers $headers -Verbose
    Write-Host "[OK] $($response.StatusCode)" -ForegroundColor Green
    Write-Host $response.Content
} catch {
    Write-Host "[FAIL] $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
    
    # Try to read the response body
    $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    $reader.BaseStream.Position = 0
    $reader.DiscardBufferedData()
    $responseBody = $reader.ReadToEnd()
    Write-Host "Response: $responseBody" -ForegroundColor Yellow
}

# Try creating a product
Write-Host "`nTesting POST /api/admin/products..." -ForegroundColor Yellow
try {
    $productBody = @{
        productName = "Test Product"
        description = "Test Description"
        minAmount = 10000
        maxAmount = 1000000
        minTenureMonths = 6
        maxTenureMonths = 60
        interestRate = 7.5
        isActive = $true
    } | ConvertTo-Json
    
    $response = Invoke-WebRequest -Uri "$baseUrl/api/admin/products" -Method POST -Headers $headers -Body $productBody -ContentType "application/json"
    Write-Host "[OK] Product created: $($response.StatusCode)" -ForegroundColor Green
} catch {
    Write-Host "[FAIL] $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}

# Test the enhanced endpoint
Write-Host "`nTesting POST /api/admin/products/enhanced..." -ForegroundColor Yellow
try {
    $productBody = @{
        productCode = "TEST001"
        productName = "Enhanced Test Product"
        description = "Test Description"
        currency = "INR"
        minAmount = 10000
        maxAmount = 1000000
        minTenureMonths = 6
        maxTenureMonths = 60
        interestRate = 7.5
        isActive = $true
    } | ConvertTo-Json
    
    $response = Invoke-WebRequest -Uri "$baseUrl/api/admin/products/enhanced" -Method POST -Headers $headers -Body $productBody -ContentType "application/json"
    Write-Host "[OK] Enhanced product created: $($response.StatusCode)" -ForegroundColor Green
    $product = $response.Content | ConvertFrom-Json
    Write-Host "Product ID: $($product.id)" -ForegroundColor Gray
} catch {
    Write-Host "[FAIL] $($_.Exception.Response.StatusCode)" -ForegroundColor Red
    Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red
}
