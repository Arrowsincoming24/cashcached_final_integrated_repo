# CashCached - Entity Relationship Diagram

## Database Schema Overview

This document describes the complete database schema for the CashCached Fixed Deposit Simulator application.

**Application Name:** CashCached (∞ CASHCACHED)  
**Version:** 1.0.0  
**Last Updated:** October 20, 2025

┌─────────────────────────────────────────────────────────────────────────────┐
│                          CASHCACHED DATABASE SCHEMA                          │
└─────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────┐
│       USERS          │
├──────────────────────┤
│ PK  id               │
│     username         │
│     email            │
│     phone_number     │
│     password         │
│     role             │◄──────────┐
│     enabled          │           │
│     preferred_currency│          │
│     preferred_language│          │
│     created_at       │           │
│     updated_at       │           │
└──────────────────────┘           │
         │                         │
         │ 1                       │
         │                         │
         │ *                       │ *
         ▼                         │
┌──────────────────────┐           │
│   FIXED_DEPOSITS     │           │
├──────────────────────┤           │
│ PK  id               │           │
│ FK  user_id          │───────────┘
│ FK  product_id       │───────────┐
│     principal_amount │           │
│     currency         │           │
│     interest_rate    │           │
│     tenure_months    │           │
│     status           │           │
│     maturity_amount  │           │
│     interest_amount  │           │
│     start_date       │           │
│     maturity_date    │           │
│     created_at       │           │
│     updated_at       │           │
└──────────────────────┘           │
         │                         │
         │ *                       │ 1
         │                         │
         ▼                         │
┌──────────────────────┐           │
│     AUDIT_LOGS       │           │
├──────────────────────┤           │
│ PK  id               │           │
│ FK  user_id          │───────────┤
│     action           │           │
│     entity_type      │           │
│     entity_id        │           │
│     old_values       │           │
│     new_values       │           │
│     ip_address       │           │
│     user_agent       │           │
│     timestamp        │           │
└──────────────────────┘           │
                                   │
                                   │
┌──────────────────────┐           │
│     FD_PRODUCTS      │◄──────────┘
├──────────────────────┤
│ PK  id               │
│     product_code     │
│     product_name     │
│     description      │
│     status           │
│     currency         │
│     min_amount       │
│     max_amount       │
│     min_tenure_months│
│     max_tenure_months│
│     interest_rate    │
│     is_active        │
│     created_at       │
│     updated_at       │
└──────────────────────┘
         │
         │ 1
         │
         │ *
         ▼
┌──────────────────────┐
│ PRODUCT_TERM_PROFILE │
├──────────────────────┤
│ PK  id               │
│ FK  product_id       │
│     term_name        │
│     term_months      │
│     min_amount       │
│     max_amount       │
│     base_interest_rate│
│     is_active        │
│     created_at       │
└──────────────────────┘

         │
         │ 1
         │
         │ *
         ▼
┌──────────────────────┐
│    RATE_MATRIX       │
├──────────────────────┤
│ PK  id               │
│ FK  product_id       │
│     customer_type    │
│     term_from_days   │
│     term_to_days     │
│     interest_rate    │
│     effective_from   │
│     effective_to     │
│     created_at       │
└──────────────────────┘

         │
         │ 1
         │
         │ *
         ▼
┌──────────────────────┐
│   BUSINESS_RULES     │
├──────────────────────┤
│ PK  id               │
│ FK  product_id       │
│ FK  rule_type_id     │
│     rule_value       │
│     is_active        │
│     created_at       │
└──────────────────────┘
         │
         │ *
         │
         │ 1
         ▼
┌──────────────────────┐
│ BUSINESS_RULE_TYPES  │
├──────────────────────┤
│ PK  id               │
│     rule_name        │
│     rule_code        │
│     description      │
│     value_type       │
│     default_value    │
└──────────────────────┘

┌──────────────────────┐
│ PRODUCT_AUDIT_LOGS   │
├──────────────────────┤
│ PK  id               │
│ FK  product_id       │
│     action           │
│     performed_by     │
│     old_values       │
│     new_values       │
│     timestamp        │
└──────────────────────┘

┌──────────────────────┐
│  OTP_VERIFICATIONS   │
├──────────────────────┤
│ PK  id               │
│     phone_number     │
│     otp_code         │
│     expiry_time      │
│     created_at       │
└──────────────────────┘
```

## Relationships

### One-to-Many Relationships

1. **USER → FIXED_DEPOSITS**
   - One user can have multiple fixed deposits
   - CASCADE: Delete user → Delete all their FDs

2. **USER → AUDIT_LOGS**
   - One user can have multiple audit log entries
   - CASCADE: Delete user → Delete their audit logs

3. **FD_PRODUCT → FIXED_DEPOSITS**
   - One product can be used in multiple FDs
   - RESTRICT: Cannot delete product if FDs exist

4. **FD_PRODUCT → PRODUCT_TERM_PROFILE**
   - One product can have multiple term profiles
   - CASCADE: Delete product → Delete term profiles

5. **FD_PRODUCT → RATE_MATRIX**
   - One product can have multiple interest rates
   - CASCADE: Delete product → Delete rate matrices

6. **FD_PRODUCT → BUSINESS_RULES**
   - One product can have multiple business rules
   - CASCADE: Delete product → Delete rules

7. **FD_PRODUCT → PRODUCT_AUDIT_LOGS**
   - One product can have multiple audit entries
   - CASCADE: Delete product → Delete audit logs

8. **BUSINESS_RULE_TYPE → BUSINESS_RULES**
   - One rule type can be used in multiple rules
   - RESTRICT: Cannot delete rule type if rules exist

## Enumerations

### Role
- `ADMIN` - Administrator access
- `CUSTOMER` - Customer access

### FdStatus
- `ACTIVE` - FD is currently active
- `MATURED` - FD has reached maturity
- `CANCELLED` - FD was cancelled before maturity

### ProductStatus
- `ACTIVE` - Product available for new FDs
- `INACTIVE` - Product not available
- `SUSPENDED` - Temporarily unavailable
- `DRAFT` - Product in draft state

### Currency
- `USD` - US Dollar
- `INR` - Indian Rupee
- `KWD` - Kuwaiti Dinar

### Language
- `EN` - English
- `AR` - Arabic

### CustomerType
- `RETAIL` - Retail customer
- `CORPORATE` - Corporate customer
- `SENIOR_CITIZEN` - Senior citizen
- `STAFF` - Bank staff

### CompoundingFrequency
- `MONTHLY` - Monthly compounding
- `QUARTERLY` - Quarterly compounding
- `HALF_YEARLY` - Half-yearly compounding
- `YEARLY` - Yearly compounding

### AuditAction
- `CREATED` - Entity created
- `UPDATED` - Entity updated
- `DELETED` - Entity deleted
- `STATUS_CHANGED` - Status changed

## Indexes

### Primary Keys
- All tables have auto-increment `id` as primary key

### Unique Constraints
- `users.username` - UNIQUE
- `users.email` - UNIQUE
- `users.phone_number` - UNIQUE
- `fd_products.product_code` - UNIQUE
- `fd_products.product_name` - UNIQUE
- `business_rule_types.rule_code` - UNIQUE

### Foreign Key Indexes
- `fixed_deposits.user_id` - INDEX
- `fixed_deposits.product_id` - INDEX
- `audit_logs.user_id` - INDEX
- `product_term_profile.product_id` - INDEX
- `rate_matrix.product_id` - INDEX
- `business_rules.product_id` - INDEX
- `business_rules.rule_type_id` - INDEX

### Search Indexes
- `fixed_deposits.status` - INDEX
- `fd_products.status` - INDEX
- `fd_products.is_active` - INDEX
- `audit_logs.timestamp` - INDEX

## Data Flow

### Customer Creates FD
```
1. Customer → Selects Product
2. System → Validates Product (status, amount, tenure)
3. System → Applies Rate Matrix (based on customer type, tenure)
4. System → Applies Business Rules
5. System → Calculates Maturity Amount
6. System → Creates Fixed Deposit
7. System → Logs Audit Entry
```

### Admin Manages Products
```
1. Admin → Creates/Updates Product
2. System → Validates Product Data
3. System → Creates Product Audit Log
4. Admin → Adds Term Profiles
5. Admin → Adds Rate Matrix
6. Admin → Adds Business Rules
7. System → Product Ready for Customers
```

### Time Travel & Batch Processing
```
1. Admin → Enables Time Travel
2. System → Sets Simulated Date
3. Admin → Fast Forwards Time
4. System → Updates Current Date
5. Admin → Processes Matured FDs
6. System → Finds FDs where maturity_date <= simulated_date
7. System → Updates FD Status to MATURED
8. System → Logs Audit Entries
```

## Database Statistics

### Current Data (Example)
- **Users:** 7 (1 admin, 6 customers)
- **Products:** 1
- **Fixed Deposits:** 0
- **Audit Logs:** Multiple entries
- **Business Rule Types:** Pre-populated
- **Term Profiles:** 0
- **Rate Matrices:** 0
- **Business Rules:** 0

## Backup & Recovery

### H2 Database Files
- **Location:** `./data/fddb.mv.db`
- **Backup:** Copy `fddb.mv.db` file
- **Restore:** Replace file and restart application

### SQL Export
```sql
-- Export all data
SCRIPT TO 'backup.sql';

-- Import data
RUNSCRIPT FROM 'backup.sql';
```

## Performance Considerations

### Optimizations
1. **Lazy Loading** - Related entities loaded on demand
2. **Indexes** - On foreign keys and search fields
3. **Connection Pooling** - HikariCP for efficient connections
4. **Caching** - Second-level cache disabled (can be enabled)

### Query Optimization
- Use pagination for large result sets
- Fetch only required fields
- Use JOIN FETCH for eager loading when needed
- Index frequently queried fields

---

**Database Engine:** H2 (Development), MySQL (Production-ready)  
**ORM:** Hibernate/JPA  
**Migration:** Hibernate DDL Auto (update mode)
