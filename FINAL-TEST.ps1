Write-Host "=== FINAL COMPREHENSIVE ENDPOINT TEST ===" -ForegroundColor Cyan
$baseUrl = "http://localhost:8080/fd-simulator"

# Login
Write-Host "`n1. Logging in..." -ForegroundColor Yellow
$loginBody = '{"username":"admin","password":"admin123"}'
$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $login.token
Write-Host "   [OK] Token received" -ForegroundColor Green

$headers = @{
    "Authorization" = "Bearer $token"
}

$successCount = 0
$failCount = 0

# Test all admin endpoints
$endpoints = @(
    @{url="/api/admin/users"; name="Users"},
    @{url="/api/admin/users/customers"; name="Customers"},
    @{url="/api/admin/fixed-deposits"; name="Fixed Deposits"},
    @{url="/api/admin/dashboard/stats"; name="Dashboard Stats"},
    @{url="/api/admin/audit-logs"; name="Audit Logs"},
    @{url="/api/admin/products"; name="Products"},
    @{url="/api/admin/products/active"; name="Active Products"},
    @{url="/api/admin/products/summary"; name="Products Summary"},
    @{url="/api/admin/time-travel/status"; name="Time Travel Status"},
    @{url="/api/admin/batch/summary-report"; name="Summary Report"}
)

Write-Host "`n2. Testing Admin Endpoints..." -ForegroundColor Yellow
foreach ($endpoint in $endpoints) {
    Write-Host "   Testing $($endpoint.name)..." -NoNewline
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl$($endpoint.url)" -Method GET -Headers $headers
        Write-Host " [OK]" -ForegroundColor Green
        $successCount++
    } catch {
        Write-Host " [FAIL] $($_.Exception.Response.StatusCode)" -ForegroundColor Red
        $failCount++
    }
}

Write-Host "`n=== RESULTS ===" -ForegroundColor Cyan
Write-Host "Success: $successCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor Red

if ($failCount -gt 0) {
    Write-Host "`n=== FAILED ENDPOINTS ===" -ForegroundColor Yellow
    Write-Host "The following endpoints are returning 404:" -ForegroundColor Yellow
    Write-Host "  - /api/admin/products" -ForegroundColor Red
    Write-Host "  - /api/admin/products/active" -ForegroundColor Red
    Write-Host "  - /api/admin/products/summary" -ForegroundColor Red
    Write-Host "`nThis indicates the product endpoints in AdminController are not being registered." -ForegroundColor Yellow
}
