# FD Simulator - Complete API Endpoints Documentation

## Base URL
```
http://localhost:8080/fd-simulator/api
```

## Authentication
All endpoints require JWT token in Authorization header:
```
Authorization: Bearer <token>
```

---

## 1. ADMIN ENDPOINTS (`/api/admin`)

### User Management
- `GET /users` - Get all users
- `GET /users/customers` - Get all customers
- `GET /users/{id}` - Get user by ID
- `PUT /users/{id}` - Update user
- `DELETE /users/{id}` - Delete user

### Fixed Deposit Management
- `GET /fixed-deposits` - Get all FDs
- `GET /fixed-deposits/status/{status}` - Get FDs by status
- `GET /fixed-deposits/{id}` - Get FD by ID
- `PUT /fixed-deposits/{id}/status` - Update FD status
- `POST /fixed-deposits/{id}/close` - Close FD

### Product Management (Basic)
- `GET /products` - Get all products
- `GET /products/active` - Get active products
- `GET /products/{id}` - Get product by ID
- `POST /products` - Create product
- `PUT /products/{id}` - Update product
- `DELETE /products/{id}` - Delete product
- `GET /products/{id}/users` - Get users of a product

### Enhanced Product Management (Team 4 Integration)
- `GET /products/summary` - Get all products summary
- `GET /products/{id}/details` - Get detailed product info
- `POST /products/enhanced` - Create product with full features
- `PUT /products/{id}/enhanced` - Update product with full features
- `PATCH /products/{id}/status` - Change product status

### Term Profile Management
- `POST /products/{productId}/term-profiles` - Add term profile
- `GET /products/{productId}/term-profiles` - Get term profiles
- `PUT /term-profiles/{profileId}` - Update term profile
- `DELETE /term-profiles/{profileId}` - Delete term profile

### Rate Matrix Management
- `POST /products/{productId}/rates` - Add interest rate
- `GET /products/{productId}/rates` - Get rates (optional: ?customerType=RETAIL)
- `PUT /rates/{rateId}` - Update rate
- `DELETE /rates/{rateId}` - Delete rate

### Business Rule Management
- `POST /products/{productId}/rules` - Add business rule
- `GET /products/{productId}/rules` - Get rules
- `PUT /rules/{ruleId}` - Update rule
- `DELETE /rules/{ruleId}` - Delete rule
- `GET /rule-types` - Get all rule types

### Time Travel Features ⏰
- `POST /time-travel/enable?date=2025-12-31` - Enable time travel
- `POST /time-travel/disable` - Disable time travel
- `POST /time-travel/fast-forward?days=30` - Fast forward time
- `GET /time-travel/status` - Get time travel status

### Batch Processing Features 🔄
- `POST /batch/process-matured` - Process all matured FDs
- `POST /batch/process-interest` - Process interest accrual
- `GET /batch/nearing-maturity?days=30` - Get FDs nearing maturity
- `GET /batch/summary-report` - Get comprehensive FD report

### Dashboard & Reports
- `GET /dashboard/stats` - Get dashboard statistics
- `GET /audit-logs` - Get all audit logs
- `GET /audit-logs/user/{userId}` - Get user audit logs

---

## 2. CUSTOMER ENDPOINTS (`/api/customer`)

### Fixed Deposit Operations
- `POST /fixed-deposits` - Create new FD
- `GET /fixed-deposits` - Get my FDs
- `GET /fixed-deposits/status/{status}` - Get my FDs by status
- `GET /fixed-deposits/{id}` - Get FD details
- `POST /fixed-deposits/{id}/close` - Close my FD

### Product Browsing
- `GET /products` - Get available products
- `GET /products/{id}` - Get product details
- `GET /products/{id}/rates` - Get product rates

### Calculator & Dashboard
- `POST /calculate` - Calculate FD returns
- `GET /dashboard/stats` - Get my dashboard stats
- `GET /audit-logs` - Get my audit logs

---

## 3. AUTHENTICATION ENDPOINTS (`/api/auth`)

- `POST /register` - Register new user
- `POST /login` - Login
- `POST /refresh` - Refresh token
- `POST /logout` - Logout

---

## Sample Requests

### 1. Enable Time Travel & Fast Forward
```bash
# Enable time travel to future date
curl -X POST "http://localhost:8080/fd-simulator/api/admin/time-travel/enable?date=2026-01-01" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Fast forward 90 days
curl -X POST "http://localhost:8080/fd-simulator/api/admin/time-travel/fast-forward?days=90" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Process matured FDs
curl -X POST "http://localhost:8080/fd-simulator/api/admin/batch/process-matured" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 2. Create Product with Rates
```bash
# Create product
curl -X POST "http://localhost:8080/fd-simulator/api/admin/products/enhanced" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "productCode": "FD001",
    "productName": "Premium FD",
    "description": "High interest FD",
    "currency": "INR",
    "status": "ACTIVE",
    "minAmount": 10000,
    "maxAmount": 1000000,
    "minTenureMonths": 12,
    "maxTenureMonths": 60,
    "interestRate": 7.5,
    "isActive": true
  }'

# Add rate matrix
curl -X POST "http://localhost:8080/fd-simulator/api/admin/products/1/rates" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "customerType": "SENIOR_CITIZEN",
    "termFromDays": 365,
    "termToDays": 730,
    "interestRate": 8.0,
    "effectiveFrom": "2025-01-01"
  }'
```

### 3. Batch Processing
```bash
# Get summary report
curl -X GET "http://localhost:8080/fd-simulator/api/admin/batch/summary-report" \
  -H "Authorization: Bearer YOUR_TOKEN"

# Find FDs nearing maturity (within 30 days)
curl -X GET "http://localhost:8080/fd-simulator/api/admin/batch/nearing-maturity?days=30" \
  -H "Authorization: Bearer YOUR_TOKEN"
```

---

## Testing Workflow

### Scenario: Test FD Maturity with Time Travel

1. **Create a test FD** (6 months tenure)
2. **Enable time travel** to 6 months in future
3. **Run batch processing** to mature the FD
4. **Verify** FD status changed to MATURED
5. **Check summary report** for updated statistics

```bash
# Step 1: Create FD (as customer)
curl -X POST "http://localhost:8080/fd-simulator/api/customer/fixed-deposits" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer CUSTOMER_TOKEN" \
  -d '{
    "principalAmount": 50000,
    "interestRate": 7.0,
    "tenureInMonths": 6,
    "currency": "INR"
  }'

# Step 2: Enable time travel (as admin)
curl -X POST "http://localhost:8080/fd-simulator/api/admin/time-travel/fast-forward?days=180" \
  -H "Authorization: Bearer ADMIN_TOKEN"

# Step 3: Process matured FDs
curl -X POST "http://localhost:8080/fd-simulator/api/admin/batch/process-matured" \
  -H "Authorization: Bearer ADMIN_TOKEN"

# Step 4: Get summary report
curl -X GET "http://localhost:8080/fd-simulator/api/admin/batch/summary-report" \
  -H "Authorization: Bearer ADMIN_TOKEN"
```

---

## Status Codes

- `200 OK` - Success
- `201 Created` - Resource created
- `204 No Content` - Success with no response body
- `400 Bad Request` - Invalid request
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## Entity Enums

### ProductStatus
- `ACTIVE` - Product is active and available
- `INACTIVE` - Product is inactive
- `SUSPENDED` - Product is temporarily suspended
- `DRAFT` - Product is in draft state

### FdStatus
- `ACTIVE` - FD is active
- `MATURED` - FD has matured
- `CANCELLED` - FD was cancelled

### CustomerType
- `RETAIL` - Retail customer
- `CORPORATE` - Corporate customer
- `SENIOR_CITIZEN` - Senior citizen
- `STAFF` - Bank staff

### CompoundingFrequency
- `MONTHLY`
- `QUARTERLY`
- `HALF_YEARLY`
- `YEARLY`

---

## Notes

- All dates are in ISO format: `YYYY-MM-DD`
- All amounts are in BigDecimal format
- Time travel affects all date-dependent operations
- Batch processing runs based on simulated date when time travel is enabled
- Admin credentials: `admin` / `admin123`
