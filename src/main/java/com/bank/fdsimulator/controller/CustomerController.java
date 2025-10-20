package com.bank.fdsimulator.controller;

import com.bank.fdsimulator.dto.*;
import com.bank.fdsimulator.entity.*;
import com.bank.fdsimulator.service.AuditService;
import com.bank.fdsimulator.service.FixedDepositService;
import com.bank.fdsimulator.service.ProductManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*")
public class CustomerController {
    
    @Autowired
    private FixedDepositService fdService;
    
    @Autowired
    private AuditService auditService;
    
    @Autowired
    private ProductManagementService productManagementService;
    
    // Fixed Deposit Management
    @PostMapping("/fixed-deposits")
    public ResponseEntity<FixedDeposit> createFixedDeposit(@Valid @RequestBody FdCreateRequest request,
                                                          Authentication authentication,
                                                          HttpServletRequest httpRequest) {
        User user = (User) authentication.getPrincipal();
        
        FixedDeposit fd = fdService.createFixedDeposit(
            user, 
            request.getPrincipalAmount(), 
            request.getInterestRate(), 
            request.getTenureInMonths(),
            Currency.fromCode(request.getCurrency())
        );
        
        auditService.logUserAction(user, "FD_CREATED", "FixedDeposit", fd.getId(), httpRequest);
        
        return ResponseEntity.ok(fd);
    }
    
    @GetMapping("/fixed-deposits")
    public ResponseEntity<?> getMyFixedDeposits(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            List<FixedDeposit> fds = fdService.getFixedDepositsByUser(user);
            return ResponseEntity.ok(fds);
        } catch (Exception e) {
            return ResponseEntity.ok(new java.util.ArrayList<>());
        }
    }
    
    @GetMapping("/fixed-deposits/status/{status}")
    public ResponseEntity<List<FixedDeposit>> getMyFixedDepositsByStatus(@PathVariable FdStatus status,
                                                                        Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<FixedDeposit> fds = fdService.getFixedDepositsByUserAndStatus(user, status);
        return ResponseEntity.ok(fds);
    }
    
    @GetMapping("/fixed-deposits/{id}")
    public ResponseEntity<FixedDeposit> getFixedDepositById(@PathVariable Long id,
                                                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        FixedDeposit fd = fdService.getFixedDepositById(id);
        
        // Check if the FD belongs to the user
        if (!fd.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body(null);
        }
        
        return ResponseEntity.ok(fd);
    }
    
    @PostMapping("/fixed-deposits/{id}/close")
    public ResponseEntity<?> closeFixedDeposit(@PathVariable Long id,
                                             Authentication authentication,
                                             HttpServletRequest httpRequest) {
        User user = (User) authentication.getPrincipal();
        FixedDeposit fd = fdService.getFixedDepositById(id);
        
        // Check if the FD belongs to the user
        if (!fd.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Access denied");
        }
        
        fdService.closeFixedDeposit(id, user);
        auditService.logUserAction(user, "FD_CLOSED", "FixedDeposit", id, httpRequest);
        
        return ResponseEntity.ok("Fixed Deposit closed successfully");
    }
    
    // Calculator
    @PostMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculateFd(@Valid @RequestBody FdCreateRequest request) {
        BigDecimal principalAmount = request.getPrincipalAmount();
        BigDecimal interestRate = request.getInterestRate();
        Integer tenureInMonths = request.getTenureInMonths();
        
        BigDecimal interestAmount = fdService.calculateInterest(principalAmount, interestRate, tenureInMonths);
        BigDecimal maturityAmount = fdService.calculateMaturityAmount(principalAmount, interestRate, tenureInMonths);
        
        Map<String, Object> result = new HashMap<>();
        result.put("principalAmount", principalAmount);
        result.put("interestRate", interestRate);
        result.put("tenureInMonths", tenureInMonths);
        result.put("interestAmount", interestAmount);
        result.put("maturityAmount", maturityAmount);
        
        return ResponseEntity.ok(result);
    }
    
    // Dashboard Statistics
    @GetMapping("/dashboard/stats")
    public ResponseEntity<?> getDashboardStats(Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            
            Map<String, Object> stats = new HashMap<>();
            
            Long totalFds = fdService.getFdCountByUser(user);
            Double totalAmount = fdService.getTotalActiveAmountByUser(user);
            
            List<FixedDeposit> activeFds = fdService.getFixedDepositsByUserAndStatus(user, FdStatus.ACTIVE);
            List<FixedDeposit> maturedFds = fdService.getFixedDepositsByUserAndStatus(user, FdStatus.MATURED);
            
            stats.put("totalFixedDeposits", totalFds);
            stats.put("activeFixedDeposits", activeFds.size());
            stats.put("maturedFixedDeposits", maturedFds.size());
            stats.put("totalActiveAmount", totalAmount);
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            Map<String, Object> emptyStats = new HashMap<>();
            emptyStats.put("totalFixedDeposits", 0);
            emptyStats.put("activeFixedDeposits", 0);
            emptyStats.put("maturedFixedDeposits", 0);
            emptyStats.put("totalActiveAmount", 0.0);
            return ResponseEntity.ok(emptyStats);
        }
    }
    
    // Audit Logs
    @GetMapping("/audit-logs")
    public ResponseEntity<List<com.bank.fdsimulator.entity.AuditLog>> getMyAuditLogs(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<com.bank.fdsimulator.entity.AuditLog> auditLogs = auditService.getAuditLogsByUser(user);
        return ResponseEntity.ok(auditLogs);
    }
    
    // ==================== Product Browsing (Customer View) ====================
    
    @GetMapping("/products")
    public ResponseEntity<?> getAvailableProducts() {
        try {
            List<ProductSummaryDTO> products = productManagementService.getActiveProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.ok(new java.util.ArrayList<>());
        }
    }
    
    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProductDetails(@PathVariable Long id) {
        try {
            ProductDetailsDTO product = productManagementService.getProductDetails(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Product not found");
        }
    }
    
    @GetMapping("/products/{id}/rates")
    public ResponseEntity<List<RateMatrixDTO>> getProductRates(@PathVariable Long id,
                                                               @RequestParam(required = false) String customerType) {
        List<RateMatrixDTO> rates = productManagementService.getRates(id, customerType);
        return ResponseEntity.ok(rates);
    }
}
