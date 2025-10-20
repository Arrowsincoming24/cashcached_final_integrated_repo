$baseUrl = "http://localhost:8080/fd-simulator"

# Login
$loginBody = '{"username":"admin","password":"admin123"}'
$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $login.token
Write-Host "Token: $($token.Substring(0,30))..." -ForegroundColor Green

$headers = @{
    "Authorization" = "Bearer $token"
}

# Test different product endpoints
Write-Host "`nTesting product endpoints..." -ForegroundColor Cyan

$endpoints = @(
    "/api/admin/products",
    "/api/admin/products/active",
    "/api/admin/products/summary"
)

foreach ($endpoint in $endpoints) {
    Write-Host "`nTesting: $endpoint" -ForegroundColor Yellow
    try {
        $response = Invoke-WebRequest -Uri "$baseUrl$endpoint" -Method GET -Headers $headers
        Write-Host "  [OK] Status: $($response.StatusCode)" -ForegroundColor Green
        $data = $response.Content | ConvertFrom-Json
        if ($data -is [array]) {
            Write-Host "  Count: $($data.Count)" -ForegroundColor Gray
        }
    } catch {
        Write-Host "  [FAIL] Status: $($_.Exception.Response.StatusCode.value__)" -ForegroundColor Red
        Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Red
    }
}
