package com.bank.fdsimulator.controller;

import com.bank.fdsimulator.dto.FdCreateRequest;
import com.bank.fdsimulator.service.FdProductService;
import com.bank.fdsimulator.service.FixedDepositService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = "*")
@Transactional(readOnly = true)
public class PublicController {
    
    private static final Logger logger = LoggerFactory.getLogger(PublicController.class);
    
    private final FdProductService productService;
    private final FixedDepositService fdService;
    
    // Constructor injection instead of field injection
    public PublicController(FdProductService productService, FixedDepositService fdService) {
        this.productService = productService;
        this.fdService = fdService;
        logger.info("🔥🔥🔥 PublicController CONSTRUCTED with services");
    }
    
    @PostConstruct
    public void init() {
        logger.info("🔥🔥🔥 PublicController INITIALIZED! Endpoints should be available at /api/public/**");
        logger.info("ProductService: {}", productService != null ? "INJECTED" : "NULL");
        logger.info("FdService: {}", fdService != null ? "INJECTED" : "NULL");
    }
    
    @GetMapping(value = "/test", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> test() {
        logger.info("✅ Test endpoint called!");
        return ResponseEntity.ok("PublicController is working!");
    }
    
    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllProducts() {
        logger.info("🔥 GET /api/public/products called");
        try {
            if (productService == null) {
                logger.error("❌ productService is NULL!");
                return ResponseEntity.ok(java.util.Collections.emptyList());
            }
            java.util.List<?> products = productService.getActiveProducts();
            logger.info("✅ Returning {} products", products.size());
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            logger.error("❌ Error: {}", e.getMessage(), e);
            return ResponseEntity.ok(java.util.Collections.emptyList());
        }
    }
    
    @PostMapping(value = "/calculate", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> calculateFd(@Valid @RequestBody FdCreateRequest request) {
        logger.info("🔥 POST /api/public/calculate called");
        try {
            if (fdService == null) {
                logger.error("❌ fdService is NULL!");
                return ResponseEntity.internalServerError().build();
            }
            
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
            result.put("currency", request.getCurrency());
            
            logger.info("✅ Calculate successful: maturity={}", maturityAmount);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            logger.error("❌ Error in calculateFd: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
