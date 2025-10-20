package com.bank.fdsimulator.controller;

import com.bank.fdsimulator.dto.*;
import com.bank.fdsimulator.entity.*;
import com.bank.fdsimulator.entity.FdProduct.ProductStatus;
import com.bank.fdsimulator.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
    @PostConstruct
    public void init() {
        logger.info("🔥🔥🔥 AdminController INITIALIZED! Endpoints should be available at /api/admin/**");
    }
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private FixedDepositService fdService;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private FdProductService productService;
    
    @Autowired
    private ProductManagementService productManagementService;
    
    @Autowired
    private TimeTravelService timeTravelService;
    
    @Autowired
    private BatchProcessingService batchProcessingService;
    
    // User Management
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/users/customers")
    public ResponseEntity<List<User>> getAllCustomers() {
        List<User> customers = userService.getActiveUsersByRole(Role.CUSTOMER);
        return ResponseEntity.ok(customers);
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user, 
                                         Authentication authentication, HttpServletRequest request) {
        // Verify user exists
        userService.findById(id);
        User currentUser = (User) authentication.getPrincipal();
        
        user.setId(id);
        User updatedUser = userService.updateUser(user);
        
        auditService.logUserAction(currentUser, "USER_UPDATED", "User", id, request);
        
        return ResponseEntity.ok(updatedUser);
    }
    
    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id, Authentication authentication, 
                                      HttpServletRequest request) {
        User currentUser = (User) authentication.getPrincipal();
        
        auditService.logUserAction(currentUser, "USER_DELETED", "User", id, request);
        userService.deleteUser(id);
        
        return ResponseEntity.ok("User deleted successfully");
    }
    
    // Fixed Deposit Management
    @GetMapping("/fixed-deposits")
    public ResponseEntity<List<FixedDeposit>> getAllFixedDeposits() {
        List<FixedDeposit> fds = fdService.getAllFixedDeposits();
        return ResponseEntity.ok(fds);
    }
    
    @GetMapping("/fixed-deposits/status/{status}")
    public ResponseEntity<List<FixedDeposit>> getFixedDepositsByStatus(@PathVariable FdStatus status) {
        List<FixedDeposit> fds = fdService.getFixedDepositsByStatus(status);
        return ResponseEntity.ok(fds);
    }
    
    @GetMapping("/fixed-deposits/{id}")
    public ResponseEntity<FixedDeposit> getFixedDepositById(@PathVariable Long id) {
        FixedDeposit fd = fdService.getFixedDepositById(id);
        return ResponseEntity.ok(fd);
    }
    
    @PutMapping("/fixed-deposits/{id}/status")
    public ResponseEntity<FixedDeposit> updateFixedDepositStatus(@PathVariable Long id, 
                                                               @RequestParam FdStatus status,
                                                               Authentication authentication, 
                                                               HttpServletRequest request) {
        User currentUser = (User) authentication.getPrincipal();
        FixedDeposit updatedFd = fdService.updateFixedDepositStatus(id, status, currentUser);
        
        auditService.logUserAction(currentUser, "FD_STATUS_UPDATED", "FixedDeposit", id, request);
        
        return ResponseEntity.ok(updatedFd);
    }
    
    @PostMapping("/fixed-deposits/{id}/close")
    public ResponseEntity<?> closeFixedDeposit(@PathVariable Long id, Authentication authentication, 
                                             HttpServletRequest request) {
        User currentUser = (User) authentication.getPrincipal();
        
        fdService.closeFixedDeposit(id, currentUser);
        auditService.logUserAction(currentUser, "FD_CLOSED", "FixedDeposit", id, request);
        
        return ResponseEntity.ok("Fixed Deposit closed successfully");
    }
    
    // Dashboard Statistics
    @GetMapping("/dashboard/stats")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        List<User> allUsers = userService.getAllUsers();
        List<User> customers = userService.getActiveUsersByRole(Role.CUSTOMER);
        List<FixedDeposit> allFds = fdService.getAllFixedDeposits();
        List<FixedDeposit> activeFds = fdService.getFixedDepositsByStatus(FdStatus.ACTIVE);
        
        stats.put("totalUsers", allUsers.size());
        stats.put("totalCustomers", customers.size());
        stats.put("totalFixedDeposits", allFds.size());
        stats.put("activeFixedDeposits", activeFds.size());
        
        // Calculate total amount
        double totalAmount = allFds.stream()
                .mapToDouble(fd -> fd.getPrincipalAmount().doubleValue())
                .sum();
        stats.put("totalAmount", totalAmount);
        
        return ResponseEntity.ok(stats);
    }
    
    // Audit Logs
    @GetMapping("/audit-logs")
    public ResponseEntity<Page<com.bank.fdsimulator.entity.AuditLog>> getAuditLogs(Pageable pageable) {
        Page<com.bank.fdsimulator.entity.AuditLog> auditLogs = auditService.getAllAuditLogs(pageable);
        return ResponseEntity.ok(auditLogs);
    }
    
    @GetMapping("/audit-logs/user/{userId}")
    public ResponseEntity<Page<com.bank.fdsimulator.entity.AuditLog>> getAuditLogsByUser(
            @PathVariable Long userId, Pageable pageable) {
        User user = userService.findById(userId);
        Page<com.bank.fdsimulator.entity.AuditLog> auditLogs = auditService.getAuditLogsByUser(user, pageable);
        return ResponseEntity.ok(auditLogs);
    }
    
    // FD Product Management
    // Note: More specific paths MUST come before generic path variables
    @GetMapping("/products-list")
    public ResponseEntity<String> testProductEndpoint() {
        return ResponseEntity.ok("Product endpoint is working!");
    }
    
    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        try {
            List<FdProduct> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/products/active")
    public ResponseEntity<?> getActiveProducts() {
        try {
            List<FdProduct> products = productService.getActiveProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }
    
    @GetMapping("/products/summary")
    public ResponseEntity<?> getAllProductsSummary() {
        try {
            List<ProductSummaryDTO> products = productManagementService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.ok(new java.util.ArrayList<>());
        }
    }
    
    @GetMapping("/products/{id:[0-9]+}")
    public ResponseEntity<FdProduct> getProductById(@PathVariable Long id) {
        FdProduct product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    
    @PostMapping("/products")
    public ResponseEntity<FdProduct> createProduct(@RequestBody FdProduct product, 
                                                   Authentication authentication, 
                                                   HttpServletRequest request) {
        User currentUser = (User) authentication.getPrincipal();
        FdProduct createdProduct = productService.createProduct(product);
        
        auditService.logUserAction(currentUser, "PRODUCT_CREATED", "FdProduct", 
                                   createdProduct.getId(), request);
        
        return ResponseEntity.ok(createdProduct);
    }
    
    @PutMapping("/products/{id:[0-9]+}")
    public ResponseEntity<FdProduct> updateProduct(@PathVariable Long id, 
                                                   @RequestBody FdProduct product,
                                                   Authentication authentication, 
                                                   HttpServletRequest request) {
        User currentUser = (User) authentication.getPrincipal();
        FdProduct updatedProduct = productService.updateProduct(id, product);
        
        auditService.logUserAction(currentUser, "PRODUCT_UPDATED", "FdProduct", id, request);
        
        return ResponseEntity.ok(updatedProduct);
    }
    
    @DeleteMapping("/products/{id:[0-9]+}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id, 
                                          Authentication authentication, 
                                          HttpServletRequest request) {
        User currentUser = (User) authentication.getPrincipal();
        
        auditService.logUserAction(currentUser, "PRODUCT_DELETED", "FdProduct", id, request);
        productService.deleteProduct(id);
        
        return ResponseEntity.ok("Product deleted successfully");
    }
    
    @GetMapping("/products/{id:[0-9]+}/users")
    public ResponseEntity<List<User>> getProductUsers(@PathVariable Long id) {
        FdProduct product = productService.getProductById(id);
        List<FixedDeposit> deposits = fdService.getFixedDepositsByProduct(product);
        
        // Extract unique users from deposits
        List<User> users = deposits.stream()
                .map(FixedDeposit::getUser)
                .distinct()
                .toList();
        
        return ResponseEntity.ok(users);
    }
    
    // ==================== Enhanced Product Management (Team 4 Integration) ====================
    
    @GetMapping("/products/{id:[0-9]+}/details")
    public ResponseEntity<ProductDetailsDTO> getProductDetailsEnhanced(@PathVariable Long id) {
        ProductDetailsDTO product = productManagementService.getProductDetails(id);
        return ResponseEntity.ok(product);
    }
    
    @PostMapping("/products/enhanced")
    public ResponseEntity<FdProduct> createProductEnhanced(@RequestBody FdProduct product, 
                                                          Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        FdProduct created = productManagementService.createProduct(product, currentUser.getEmail());
        return ResponseEntity.ok(created);
    }
    
    @PutMapping("/products/{id:[0-9]+}/enhanced")
    public ResponseEntity<FdProduct> updateProductEnhanced(@PathVariable Long id, 
                                                          @RequestBody FdProduct product,
                                                          Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        FdProduct updated = productManagementService.updateProduct(id, product, currentUser.getEmail());
        return ResponseEntity.ok(updated);
    }
    
    @PatchMapping("/products/{id:[0-9]+}/status")
    public ResponseEntity<FdProduct> changeProductStatus(@PathVariable Long id, 
                                                        @RequestParam ProductStatus status,
                                                        Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        FdProduct updated = productManagementService.changeStatus(id, status, currentUser.getEmail());
        return ResponseEntity.ok(updated);
    }
    
    // ==================== Term Profile Management ====================
    
    @PostMapping("/products/{productId}/term-profiles")
    public ResponseEntity<ProductTermProfileDTO> addTermProfile(@PathVariable Long productId, 
                                                               @RequestBody ProductTermProfileDTO dto) {
        ProductTermProfileDTO created = productManagementService.addTermProfile(productId, dto);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/products/{productId}/term-profiles")
    public ResponseEntity<List<ProductTermProfileDTO>> getTermProfiles(@PathVariable Long productId) {
        List<ProductTermProfileDTO> profiles = productManagementService.getTermProfiles(productId);
        return ResponseEntity.ok(profiles);
    }
    
    @PutMapping("/term-profiles/{profileId}")
    public ResponseEntity<ProductTermProfileDTO> updateTermProfile(@PathVariable Long profileId, 
                                                                   @RequestBody ProductTermProfileDTO dto) {
        ProductTermProfileDTO updated = productManagementService.updateTermProfile(profileId, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/term-profiles/{profileId}")
    public ResponseEntity<Void> deleteTermProfile(@PathVariable Long profileId) {
        productManagementService.deleteTermProfile(profileId);
        return ResponseEntity.noContent().build();
    }
    
    // ==================== Rate Matrix Management ====================
    
    @PostMapping("/products/{productId}/rates")
    public ResponseEntity<RateMatrixDTO> addRate(@PathVariable Long productId, 
                                                 @RequestBody RateMatrixDTO dto) {
        RateMatrixDTO created = productManagementService.addRate(productId, dto);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/products/{productId}/rates")
    public ResponseEntity<List<RateMatrixDTO>> getRates(@PathVariable Long productId, 
                                                        @RequestParam(required = false) String customerType) {
        List<RateMatrixDTO> rates = productManagementService.getRates(productId, customerType);
        return ResponseEntity.ok(rates);
    }
    
    @PutMapping("/rates/{rateId}")
    public ResponseEntity<RateMatrixDTO> updateRate(@PathVariable Long rateId, 
                                                    @RequestBody RateMatrixDTO dto) {
        RateMatrixDTO updated = productManagementService.updateRate(rateId, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/rates/{rateId}")
    public ResponseEntity<Void> deleteRate(@PathVariable Long rateId) {
        productManagementService.deleteRate(rateId);
        return ResponseEntity.noContent().build();
    }
    
    // ==================== Business Rule Management ====================
    
    @PostMapping("/products/{productId}/rules")
    public ResponseEntity<BusinessRuleDTO> addRule(@PathVariable Long productId, 
                                                   @RequestBody BusinessRuleDTO dto) {
        BusinessRuleDTO created = productManagementService.addRule(productId, dto);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/products/{productId}/rules")
    public ResponseEntity<List<BusinessRuleDTO>> getRules(@PathVariable Long productId) {
        List<BusinessRuleDTO> rules = productManagementService.getRules(productId);
        return ResponseEntity.ok(rules);
    }
    
    @PutMapping("/rules/{ruleId}")
    public ResponseEntity<BusinessRuleDTO> updateRule(@PathVariable Long ruleId, 
                                                      @RequestBody BusinessRuleDTO dto) {
        BusinessRuleDTO updated = productManagementService.updateRule(ruleId, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/rules/{ruleId}")
    public ResponseEntity<Void> deleteRule(@PathVariable Long ruleId) {
        productManagementService.deleteRule(ruleId);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/rule-types")
    public ResponseEntity<List<BusinessRuleType>> getAllRuleTypes() {
        List<BusinessRuleType> ruleTypes = productManagementService.getAllRuleTypes();
        return ResponseEntity.ok(ruleTypes);
    }
    
    // ==================== Time Travel & Batch Processing ====================
    
    @PostMapping("/time-travel/enable")
    public ResponseEntity<Map<String, Object>> enableTimeTravel(@RequestParam String date) {
        LocalDate targetDate = LocalDate.parse(date);
        timeTravelService.enableTimeTravel(targetDate);
        
        Map<String, Object> response = new HashMap<>();
        response.put("enabled", true);
        response.put("simulatedDate", targetDate);
        response.put("message", "Time travel enabled. Current simulated date: " + targetDate);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/time-travel/disable")
    public ResponseEntity<Map<String, Object>> disableTimeTravel() {
        timeTravelService.disableTimeTravel();
        
        Map<String, Object> response = new HashMap<>();
        response.put("enabled", false);
        response.put("currentDate", LocalDate.now());
        response.put("message", "Time travel disabled. Using current date.");
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/time-travel/fast-forward")
    public ResponseEntity<Map<String, Object>> fastForward(@RequestParam int days) {
        timeTravelService.fastForward(days);
        
        Map<String, Object> response = new HashMap<>();
        response.put("enabled", timeTravelService.isTimeTravelEnabled());
        response.put("simulatedDate", timeTravelService.getCurrentDate());
        response.put("message", "Fast forwarded " + days + " days");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/time-travel/status")
    public ResponseEntity<Map<String, Object>> getTimeTravelStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("enabled", timeTravelService.isTimeTravelEnabled());
        response.put("currentDate", timeTravelService.getCurrentDate());
        response.put("simulatedDate", timeTravelService.getSimulatedDate());
        response.put("realDate", LocalDate.now());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/batch/process-matured")
    public ResponseEntity<Map<String, Object>> processMaturedDeposits() {
        var result = batchProcessingService.processMaturedDeposits();
        
        Map<String, Object> response = new HashMap<>();
        response.put("processedCount", result.getProcessedCount());
        response.put("errorCount", result.getErrorCount());
        response.put("totalAmount", result.getTotalAmount());
        response.put("successful", result.isSuccessful());
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/batch/process-interest")
    public ResponseEntity<Map<String, Object>> processInterestAccrual() {
        var result = batchProcessingService.processInterestAccrual();
        
        Map<String, Object> response = new HashMap<>();
        response.put("processedCount", result.getProcessedCount());
        response.put("errorCount", result.getErrorCount());
        response.put("totalInterest", result.getTotalAmount());
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/batch/nearing-maturity")
    public ResponseEntity<List<FixedDeposit>> getFdsNearingMaturity(@RequestParam(defaultValue = "30") int days) {
        List<FixedDeposit> fds = batchProcessingService.findFdsNearingMaturity(days);
        return ResponseEntity.ok(fds);
    }
    
    @GetMapping("/batch/summary-report")
    public ResponseEntity<Map<String, Object>> getSummaryReport() {
        var report = batchProcessingService.generateSummaryReport();
        
        Map<String, Object> response = new HashMap<>();
        response.put("reportDate", report.getReportDate());
        response.put("totalFds", report.getTotalFds());
        response.put("activeFds", report.getActiveFds());
        response.put("maturedFds", report.getMaturedFds());
        response.put("closedFds", report.getClosedFds());
        response.put("totalPrincipal", report.getTotalPrincipal());
        response.put("totalMaturityValue", report.getTotalMaturityValue());
        
        return ResponseEntity.ok(response);
    }
}
