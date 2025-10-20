$baseUrl = "http://localhost:8080/fd-simulator"

Write-Host "=== DEBUGGING FAILING ENDPOINTS ===" -ForegroundColor Cyan

# Login
Write-Host "`n1. Logging in..." -ForegroundColor Yellow
try {
    $loginBody = '{"username":"admin","password":"admin123"}'
    $login = Invoke-WebRequest -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
    $loginData = $login.Content | ConvertFrom-Json
    $token = $loginData.token
    Write-Host "   [OK] Token: $($token.Substring(0,30))..." -ForegroundColor Green
} catch {
    Write-Host "   [FAIL] Login failed" -ForegroundColor Red
    exit
}

$headers = @{
    "Authorization" = "Bearer $token"
}

# Test /api/admin/users
Write-Host "`n2. Testing GET /api/admin/users..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/api/admin/users" -Method GET -Headers $headers
    Write-Host "   [OK] Status: $($response.StatusCode)" -ForegroundColor Green
    $users = $response.Content | ConvertFrom-Json
    Write-Host "   Users count: $($users.Count)" -ForegroundColor Gray
} catch {
    Write-Host "   [FAIL] Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.ErrorDetails.Message) {
        Write-Host "   Details: $($_.ErrorDetails.Message)" -ForegroundColor Red
    }
}

# Test /api/admin/products
Write-Host "`n3. Testing GET /api/admin/products..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/api/admin/products" -Method GET -Headers $headers
    Write-Host "   [OK] Status: $($response.StatusCode)" -ForegroundColor Green
    $products = $response.Content | ConvertFrom-Json
    Write-Host "   Products count: $($products.Count)" -ForegroundColor Gray
} catch {
    Write-Host "   [FAIL] Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.ErrorDetails.Message) {
        Write-Host "   Details: $($_.ErrorDetails.Message)" -ForegroundColor Red
    }
}

# Test /api/admin/audit-logs
Write-Host "`n4. Testing GET /api/admin/audit-logs..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/api/admin/audit-logs" -Method GET -Headers $headers
    Write-Host "   [OK] Status: $($response.StatusCode)" -ForegroundColor Green
    Write-Host "   Response: $($response.Content.Substring(0, 100))..." -ForegroundColor Gray
} catch {
    Write-Host "   [FAIL] Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.ErrorDetails.Message) {
        Write-Host "   Details: $($_.ErrorDetails.Message)" -ForegroundColor Red
    }
}

# Test /api/admin/fixed-deposits
Write-Host "`n5. Testing GET /api/admin/fixed-deposits..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "$baseUrl/api/admin/fixed-deposits" -Method GET -Headers $headers
    Write-Host "   [OK] Status: $($response.StatusCode)" -ForegroundColor Green
    $fds = $response.Content | ConvertFrom-Json
    Write-Host "   FDs count: $($fds.Count)" -ForegroundColor Gray
} catch {
    Write-Host "   [FAIL] Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host "`n=== DEBUG COMPLETE ===" -ForegroundColor Cyan
