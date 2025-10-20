# Quick Test Script for FD Simulator

## Prerequisites
- Application running on `http://localhost:8080/fd-simulator`
- Admin credentials: `admin` / `admin123`

---

## Test 1: Login and Get Token

```bash
# Login as admin
curl -X POST "http://localhost:8080/fd-simulator/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'

# Save the token from response
# TOKEN="<your_token_here>"
```

---

## Test 2: Verify Product Management

```bash
# Get all products summary
curl -X GET "http://localhost:8080/fd-simulator/api/admin/products/summary" \
  -H "Authorization: Bearer $TOKEN"

# Get product details (product ID 1)
curl -X GET "http://localhost:8080/fd-simulator/api/admin/products/1/details" \
  -H "Authorization: Bearer $TOKEN"

# Get all business rule types
curl -X GET "http://localhost:8080/fd-simulator/api/admin/rule-types" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Test 3: Time Travel Feature

```bash
# Check current time travel status
curl -X GET "http://localhost:8080/fd-simulator/api/admin/time-travel/status" \
  -H "Authorization: Bearer $TOKEN"

# Enable time travel to specific date
curl -X POST "http://localhost:8080/fd-simulator/api/admin/time-travel/enable?date=2026-01-01" \
  -H "Authorization: Bearer $TOKEN"

# Fast forward 30 days
curl -X POST "http://localhost:8080/fd-simulator/api/admin/time-travel/fast-forward?days=30" \
  -H "Authorization: Bearer $TOKEN"

# Check status again
curl -X GET "http://localhost:8080/fd-simulator/api/admin/time-travel/status" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Test 4: Create and Manage Product

```bash
# Create new product
curl -X POST "http://localhost:8080/fd-simulator/api/admin/products/enhanced" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "productCode": "TEST001",
    "productName": "Test Premium FD",
    "description": "Test product for integration",
    "currency": "INR",
    "status": "ACTIVE",
    "minAmount": 50000,
    "maxAmount": 5000000,
    "minTenureMonths": 12,
    "maxTenureMonths": 60,
    "interestRate": 7.5,
    "isActive": true
  }'

# Add rate matrix (replace {productId} with actual ID from response)
curl -X POST "http://localhost:8080/fd-simulator/api/admin/products/11/rates" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "customerType": "SENIOR_CITIZEN",
    "termFromDays": 365,
    "termToDays": 1825,
    "interestRate": 8.0,
    "effectiveFrom": "2025-01-01"
  }'

# Add another rate for retail customers
curl -X POST "http://localhost:8080/fd-simulator/api/admin/products/11/rates" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "customerType": "RETAIL",
    "termFromDays": 365,
    "termToDays": 1825,
    "interestRate": 7.5,
    "effectiveFrom": "2025-01-01"
  }'

# Get rates for the product
curl -X GET "http://localhost:8080/fd-simulator/api/admin/products/11/rates" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Test 5: Batch Processing

```bash
# Get summary report
curl -X GET "http://localhost:8080/fd-simulator/api/admin/batch/summary-report" \
  -H "Authorization: Bearer $TOKEN"

# Process matured FDs
curl -X POST "http://localhost:8080/fd-simulator/api/admin/batch/process-matured" \
  -H "Authorization: Bearer $TOKEN"

# Find FDs nearing maturity (within 30 days)
curl -X GET "http://localhost:8080/fd-simulator/api/admin/batch/nearing-maturity?days=30" \
  -H "Authorization: Bearer $TOKEN"

# Process interest accrual
curl -X POST "http://localhost:8080/fd-simulator/api/admin/batch/process-interest" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Test 6: Customer View (Create Customer First)

```bash
# Register as customer
curl -X POST "http://localhost:8080/fd-simulator/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testcustomer",
    "email": "customer@test.com",
    "password": "password123",
    "role": "CUSTOMER"
  }'

# Login as customer
curl -X POST "http://localhost:8080/fd-simulator/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testcustomer",
    "password": "password123"
  }'

# Save customer token
# CUSTOMER_TOKEN="<customer_token_here>"

# Browse products
curl -X GET "http://localhost:8080/fd-simulator/api/customer/products" \
  -H "Authorization: Bearer $CUSTOMER_TOKEN"

# View product details
curl -X GET "http://localhost:8080/fd-simulator/api/customer/products/1" \
  -H "Authorization: Bearer $CUSTOMER_TOKEN"

# Create FD
curl -X POST "http://localhost:8080/fd-simulator/api/customer/fixed-deposits" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $CUSTOMER_TOKEN" \
  -d '{
    "principalAmount": 100000,
    "interestRate": 7.0,
    "tenureInMonths": 12,
    "currency": "INR"
  }'

# Get my FDs
curl -X GET "http://localhost:8080/fd-simulator/api/customer/fixed-deposits" \
  -H "Authorization: Bearer $CUSTOMER_TOKEN"
```

---

## Test 7: Complete Time Travel Scenario

```bash
# As admin, enable time travel
curl -X POST "http://localhost:8080/fd-simulator/api/admin/time-travel/enable?date=2025-01-20" \
  -H "Authorization: Bearer $TOKEN"

# Fast forward 12 months (to mature the FD created above)
curl -X POST "http://localhost:8080/fd-simulator/api/admin/time-travel/fast-forward?days=365" \
  -H "Authorization: Bearer $TOKEN"

# Process matured FDs
curl -X POST "http://localhost:8080/fd-simulator/api/admin/batch/process-matured" \
  -H "Authorization: Bearer $TOKEN"

# Check summary report
curl -X GET "http://localhost:8080/fd-simulator/api/admin/batch/summary-report" \
  -H "Authorization: Bearer $TOKEN"

# Disable time travel
curl -X POST "http://localhost:8080/fd-simulator/api/admin/time-travel/disable" \
  -H "Authorization: Bearer $TOKEN"
```

---

## Test 8: Dashboard Statistics

```bash
# Admin dashboard stats
curl -X GET "http://localhost:8080/fd-simulator/api/admin/dashboard/stats" \
  -H "Authorization: Bearer $TOKEN"

# Customer dashboard stats
curl -X GET "http://localhost:8080/fd-simulator/api/customer/dashboard/stats" \
  -H "Authorization: Bearer $CUSTOMER_TOKEN"
```

---

## Expected Results

### ✅ All tests should return:
- Status 200 OK
- Valid JSON responses
- No error messages

### ✅ Time Travel should:
- Change simulated date
- Affect batch processing
- Process FDs based on simulated time

### ✅ Batch Processing should:
- Update FD statuses correctly
- Calculate maturity amounts
- Generate accurate reports

### ✅ Product Management should:
- Create products with all fields
- Add/update/delete rates
- Manage business rules
- Track changes in audit log

---

## Troubleshooting

### Issue: 401 Unauthorized
- **Solution**: Token expired, login again

### Issue: 404 Not Found
- **Solution**: Check endpoint URL and product IDs

### Issue: 500 Internal Server Error
- **Solution**: Check application logs in console

### Issue: No products returned
- **Solution**: Verify DataInitializer ran successfully

---

## Quick Verification Checklist

- [ ] Application starts without errors
- [ ] Can login as admin
- [ ] Products are pre-loaded (10 products)
- [ ] Business rule types are created (5 types)
- [ ] Can create new product
- [ ] Can add rates to product
- [ ] Time travel enables successfully
- [ ] Fast forward works
- [ ] Batch processing runs
- [ ] Summary report generates
- [ ] Customer can browse products
- [ ] Customer can create FD
- [ ] FD matures after time travel

---

## PowerShell Version (Windows)

```powershell
# Set token variable
$TOKEN = "your_token_here"

# Get products
$headers = @{
    "Authorization" = "Bearer $TOKEN"
}
Invoke-RestMethod -Uri "http://localhost:8080/fd-simulator/api/admin/products/summary" -Headers $headers

# Enable time travel
Invoke-RestMethod -Uri "http://localhost:8080/fd-simulator/api/admin/time-travel/enable?date=2026-01-01" -Method Post -Headers $headers

# Process matured FDs
Invoke-RestMethod -Uri "http://localhost:8080/fd-simulator/api/admin/batch/process-matured" -Method Post -Headers $headers
```

---

**All tests passing = Integration successful! ✅**
