Write-Host "=== TESTING ALL ENDPOINTS ===" -ForegroundColor Cyan

# Login
$response = Invoke-RestMethod -Uri "http://localhost:8080/fd-simulator/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body '{"username":"admin","password":"admin123"}'
$token = $response.token
Write-Host "`n1. Login: SUCCESS" -ForegroundColor Green
Write-Host "   Role: $($response.role)"

# Test Admin Users
try {
    $users = Invoke-RestMethod -Uri "http://localhost:8080/fd-simulator/api/admin/users" -Method GET -Headers @{"Authorization"="Bearer $token"}
    Write-Host "`n2. /api/admin/users: SUCCESS - $($users.Count) users" -ForegroundColor Green
    $users | Select-Object -First 3 username, email, role | Format-Table -AutoSize
} catch {
    Write-Host "`n2. /api/admin/users: FAILED" -ForegroundColor Red
    Write-Host "   Error: $($_.Exception.Message)"
}

# Test Admin Products
try {
    $products = Invoke-RestMethod -Uri "http://localhost:8080/fd-simulator/api/admin/products" -Method GET -Headers @{"Authorization"="Bearer $token"}
    Write-Host "`n3. /api/admin/products: SUCCESS - $($products.Count) products" -ForegroundColor Green
} catch {
    Write-Host "`n3. /api/admin/products: FAILED" -ForegroundColor Red
}

# Test Dashboard Stats
try {
    $stats = Invoke-RestMethod -Uri "http://localhost:8080/fd-simulator/api/admin/dashboard/stats" -Method GET -Headers @{"Authorization"="Bearer $token"}
    Write-Host "`n4. /api/admin/dashboard/stats: SUCCESS" -ForegroundColor Green
    $stats | Format-List
} catch {
    Write-Host "`n4. /api/admin/dashboard/stats: FAILED" -ForegroundColor Red
}

Write-Host "`n=== TEST COMPLETE ===" -ForegroundColor Cyan
