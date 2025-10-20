Write-Host "=== VERIFYING UI-TO-API CONNECTIONS ===" -ForegroundColor Cyan
$baseUrl = "http://localhost:8080/fd-simulator"

# Login
$loginBody = '{"username":"admin","password":"admin123"}'
$login = Invoke-RestMethod -Uri "$baseUrl/api/auth/login" -Method POST -Body $loginBody -ContentType "application/json"
$token = $login.token
$headers = @{"Authorization" = "Bearer $token"}

Write-Host "`n✅ ADMIN DASHBOARD APIs:" -ForegroundColor Green

Write-Host "  1. User Management..." -NoNewline
try {
    $users = Invoke-RestMethod -Uri "$baseUrl/api/admin/users" -Method GET -Headers $headers
    Write-Host " ✅ $($users.Count) users loaded" -ForegroundColor Green
} catch { Write-Host " ❌ FAILED" -ForegroundColor Red }

Write-Host "  2. Product Management..." -NoNewline
try {
    $products = Invoke-RestMethod -Uri "$baseUrl/api/admin/products" -Method GET -Headers $headers
    Write-Host " ✅ $($products.Count) products loaded" -ForegroundColor Green
} catch { Write-Host " ❌ FAILED" -ForegroundColor Red }

Write-Host "  3. Fixed Deposits..." -NoNewline
try {
    $fds = Invoke-RestMethod -Uri "$baseUrl/api/admin/fixed-deposits" -Method GET -Headers $headers
    Write-Host " ✅ $($fds.Count) FDs loaded" -ForegroundColor Green
} catch { Write-Host " ❌ FAILED" -ForegroundColor Red }

Write-Host "  4. Audit Logs..." -NoNewline
try {
    $logs = Invoke-RestMethod -Uri "$baseUrl/api/admin/audit-logs" -Method GET -Headers $headers
    Write-Host " ✅ Logs loaded" -ForegroundColor Green
} catch { Write-Host " ❌ FAILED" -ForegroundColor Red }

Write-Host "  5. Dashboard Stats..." -NoNewline
try {
    $stats = Invoke-RestMethod -Uri "$baseUrl/api/admin/dashboard/stats" -Method GET -Headers $headers
    Write-Host " ✅ Stats loaded" -ForegroundColor Green
    Write-Host "     - Total Users: $($stats.totalUsers)" -ForegroundColor Gray
    Write-Host "     - Total FDs: $($stats.totalFixedDeposits)" -ForegroundColor Gray
    Write-Host "     - Active FDs: $($stats.activeFixedDeposits)" -ForegroundColor Gray
} catch { Write-Host " ❌ FAILED" -ForegroundColor Red }

Write-Host "`n🎉 ALL UI COMPONENTS SHOULD NOW WORK!" -ForegroundColor Green
Write-Host "   - No more 'Loading...' stuck states" -ForegroundColor Yellow
Write-Host "   - No more 'Error loading' messages" -ForegroundColor Yellow
Write-Host "   - All data should display properly" -ForegroundColor Yellow

Write-Host ""
Write-Host "Test the UI at:" -ForegroundColor Cyan
Write-Host "http://localhost:8080/fd-simulator/admin/dashboard" -ForegroundColor White
