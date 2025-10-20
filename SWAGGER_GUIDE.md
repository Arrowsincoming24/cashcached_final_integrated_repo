# 📚 Swagger UI Testing Guide

## 🌐 Access Swagger UI

**URL:** http://localhost:8080/fd-simulator/swagger-ui/index.html

---

## 🔐 Authentication Setup

### Step 1: Get JWT Token
1. In Swagger UI, find the **auth-controller** section
2. Expand `POST /api/auth/login`
3. Click "Try it out"
4. Enter request body:
```json
{
  "username": "admin",
  "password": "admin123"
}
```
5. Click "Execute"
6. Copy the `token` value from the response

### Step 2: Authorize
1. Click the **"Authorize"** button at the top right
2. In the "Value" field, enter: `Bearer YOUR_TOKEN_HERE`
   - Example: `Bearer eyJhbGciOiJIUzI1NiJ9...`
3. Click "Authorize"
4. Click "Close"

✅ You're now authenticated! All endpoints will use this token.

---

## 🧪 Testing Endpoints

### Test User Management
1. Go to **admin-controller** section
2. Expand `GET /api/admin/users`
3. Click "Try it out"
4. Click "Execute"
5. ✅ Should return list of 7 users

### Test Product Management
1. Expand `GET /api/admin/products`
2. Click "Try it out"
3. Click "Execute"
4. ✅ Should return list of products

### Create a New Product
1. Expand `POST /api/admin/products/enhanced`
2. Click "Try it out"
3. Enter request body:
```json
{
  "productCode": "FD_PREMIUM_001",
  "productName": "Premium Fixed Deposit",
  "description": "High-interest fixed deposit for premium customers",
  "currency": "INR",
  "status": "ACTIVE",
  "minAmount": 50000,
  "maxAmount": 10000000,
  "minTenureMonths": 12,
  "maxTenureMonths": 60,
  "interestRate": 8.5,
  "isActive": true
}
```
4. Click "Execute"
5. ✅ Product created successfully

### Test Time Travel
1. Expand `POST /api/admin/time-travel/enable`
2. Click "Try it out"
3. Enter date parameter: `2026-01-01`
4. Click "Execute"
5. ✅ Time travel enabled

6. Expand `POST /api/admin/time-travel/fast-forward`
7. Enter days parameter: `90`
8. Click "Execute"
9. ✅ Time fast-forwarded 90 days

10. Check status: `GET /api/admin/time-travel/status`
11. ✅ Should show current simulated date

### Test Batch Processing
1. Expand `POST /api/admin/batch/process-matured`
2. Click "Try it out"
3. Click "Execute"
4. ✅ Processes all matured FDs

5. Expand `GET /api/admin/batch/summary-report`
6. Click "Execute"
7. ✅ Returns comprehensive FD report

---

## 📊 Common Test Scenarios

### Scenario 1: Create and Test FD Maturity
```
1. Create a product (if not exists)
2. Create an FD with 6 months tenure (as customer)
3. Enable time travel
4. Fast forward 180 days
5. Process matured FDs
6. Check FD status (should be MATURED)
7. View summary report
8. Disable time travel
```

### Scenario 2: Product Management Workflow
```
1. GET /api/admin/products - View all products
2. POST /api/admin/products/enhanced - Create new product
3. GET /api/admin/products/{id}/details - View product details
4. POST /api/admin/products/{id}/rates - Add interest rates
5. GET /api/admin/products/{id}/rates - View rates
6. PUT /api/admin/products/{id}/enhanced - Update product
7. PATCH /api/admin/products/{id}/status - Change status
```

### Scenario 3: Customer Journey
```
1. POST /api/auth/register - Register as customer
2. POST /api/auth/login - Login
3. GET /api/customer/products - Browse products
4. POST /api/customer/calculate - Calculate returns
5. POST /api/customer/fixed-deposits - Create FD
6. GET /api/customer/fixed-deposits - View my FDs
7. GET /api/customer/dashboard/stats - View dashboard
```

---

## 🎯 Response Codes

- **200 OK** - Success
- **201 Created** - Resource created
- **400 Bad Request** - Invalid input
- **401 Unauthorized** - Not authenticated
- **403 Forbidden** - Not authorized
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error

---

## 💡 Tips

### Refresh Token
If you get 401 errors:
1. Login again to get a new token
2. Click "Authorize" and update the token
3. Tokens expire after 2 minutes (configurable in application.yml)

### View Request/Response
- Click on any endpoint to see:
  - Request parameters
  - Request body schema
  - Response schema
  - Example values

### Test Multiple Scenarios
- Use "Try it out" to test different inputs
- Check response codes and messages
- Verify data in H2 console if needed

### Export API Documentation
- Click "Download" at the top
- Choose OpenAPI JSON or YAML format
- Use for Postman, Insomnia, or other tools

---

## 🔍 Troubleshooting

### Swagger UI Not Loading
- Check URL: http://localhost:8080/fd-simulator/swagger-ui/index.html
- Verify application is running
- Check browser console for errors

### 401 Unauthorized
- Token expired - login again
- Token not set - click "Authorize"
- Wrong token format - must be "Bearer TOKEN"

### 404 Not Found
- Check endpoint URL is correct
- Verify path parameters are provided
- Ensure application is running

### 500 Internal Server Error
- Check application logs
- Verify request body format
- Check required fields are provided

---

## 📝 Quick Reference

### Base URLs
- **API:** http://localhost:8080/fd-simulator/api
- **Swagger:** http://localhost:8080/fd-simulator/swagger-ui/index.html
- **API Docs:** http://localhost:8080/fd-simulator/v3/api-docs

### Controllers
- **auth-controller** - Authentication endpoints
- **admin-controller** - Admin management endpoints
- **customer-controller** - Customer endpoints
- **public-controller** - Public endpoints
- **web-controller** - Web page routes

### Authentication
- **Type:** Bearer Token (JWT)
- **Header:** Authorization: Bearer {token}
- **Expiry:** 2 minutes (configurable)

---

**Happy Testing! 🚀**

Use Swagger UI to explore and test all API endpoints interactively!
