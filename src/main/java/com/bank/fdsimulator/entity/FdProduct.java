package com.bank.fdsimulator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fd_products")
public class FdProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_code", length = 50, unique = true)
    private String productCode;
    
    @NotBlank(message = "Product name is required")
    @Column(nullable = false, unique = true)
    private String productName;
    
    @Column(length = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private ProductStatus status = ProductStatus.ACTIVE;
    
    @Column(length = 10, nullable = false)
    private String currency = "INR";
    
    @NotNull(message = "Minimum amount is required")
    @DecimalMin(value = "1000.0", message = "Minimum amount must be at least 1000")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal minAmount;
    
    @NotNull(message = "Maximum amount is required")
    @DecimalMin(value = "1000.0", message = "Maximum amount must be at least 1000")
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal maxAmount;
    
    @NotNull(message = "Minimum tenure is required")
    @Min(value = 1, message = "Minimum tenure must be at least 1 month")
    @Column(nullable = false)
    private Integer minTenureMonths;
    
    @NotNull(message = "Maximum tenure is required")
    @Min(value = 1, message = "Maximum tenure must be at least 1 month")
    @Column(nullable = false)
    private Integer maxTenureMonths;
    
    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.0", message = "Interest rate must be positive")
    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    // Currency-specific amount ranges
    @Column(precision = 15, scale = 2)
    private BigDecimal minAmountUSD;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal maxAmountUSD;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal minAmountINR;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal maxAmountINR;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal minAmountKWD;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal maxAmountKWD;
    
    // Relationships
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductTermProfile> termProfiles = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RateMatrix> rateMatrices = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BusinessRule> businessRules = new ArrayList<>();
    
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductAuditLog> auditLogs = new ArrayList<>();
    
    @CreationTimestamp
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    // Constructors
    public FdProduct() {}
    
    public FdProduct(String productName, String description, BigDecimal minAmount, 
                    BigDecimal maxAmount, Integer minTenureMonths, Integer maxTenureMonths, 
                    BigDecimal interestRate) {
        this.productName = productName;
        this.description = description;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.minTenureMonths = minTenureMonths;
        this.maxTenureMonths = maxTenureMonths;
        this.interestRate = interestRate;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProductName() {
        return productName;
    }
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public BigDecimal getMinAmount() {
        return minAmount;
    }
    
    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }
    
    public BigDecimal getMaxAmount() {
        return maxAmount;
    }
    
    public void setMaxAmount(BigDecimal maxAmount) {
        this.maxAmount = maxAmount;
    }
    
    public Integer getMinTenureMonths() {
        return minTenureMonths;
    }
    
    public void setMinTenureMonths(Integer minTenureMonths) {
        this.minTenureMonths = minTenureMonths;
    }
    
    public Integer getMaxTenureMonths() {
        return maxTenureMonths;
    }
    
    public void setMaxTenureMonths(Integer maxTenureMonths) {
        this.maxTenureMonths = maxTenureMonths;
    }
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Currency-specific getters and setters
    public BigDecimal getMinAmountUSD() {
        return minAmountUSD;
    }
    
    public void setMinAmountUSD(BigDecimal minAmountUSD) {
        this.minAmountUSD = minAmountUSD;
    }
    
    public BigDecimal getMaxAmountUSD() {
        return maxAmountUSD;
    }
    
    public void setMaxAmountUSD(BigDecimal maxAmountUSD) {
        this.maxAmountUSD = maxAmountUSD;
    }
    
    public BigDecimal getMinAmountINR() {
        return minAmountINR;
    }
    
    public void setMinAmountINR(BigDecimal minAmountINR) {
        this.minAmountINR = minAmountINR;
    }
    
    public BigDecimal getMaxAmountINR() {
        return maxAmountINR;
    }
    
    public void setMaxAmountINR(BigDecimal maxAmountINR) {
        this.maxAmountINR = maxAmountINR;
    }
    
    public BigDecimal getMinAmountKWD() {
        return minAmountKWD;
    }
    
    public void setMinAmountKWD(BigDecimal minAmountKWD) {
        this.minAmountKWD = minAmountKWD;
    }
    
    public BigDecimal getMaxAmountKWD() {
        return maxAmountKWD;
    }
    
    public void setMaxAmountKWD(BigDecimal maxAmountKWD) {
        this.maxAmountKWD = maxAmountKWD;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }
    
    public ProductStatus getStatus() {
        return status;
    }
    
    public void setStatus(ProductStatus status) {
        this.status = status;
    }
    
    public String getCurrency() {
        return currency;
    }
    
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public List<ProductTermProfile> getTermProfiles() {
        return termProfiles;
    }
    
    public void setTermProfiles(List<ProductTermProfile> termProfiles) {
        this.termProfiles = termProfiles;
    }
    
    public List<RateMatrix> getRateMatrices() {
        return rateMatrices;
    }
    
    public void setRateMatrices(List<RateMatrix> rateMatrices) {
        this.rateMatrices = rateMatrices;
    }
    
    public List<BusinessRule> getBusinessRules() {
        return businessRules;
    }
    
    public void setBusinessRules(List<BusinessRule> businessRules) {
        this.businessRules = businessRules;
    }
    
    public List<ProductAuditLog> getAuditLogs() {
        return auditLogs;
    }
    
    public void setAuditLogs(List<ProductAuditLog> auditLogs) {
        this.auditLogs = auditLogs;
    }
    
    // Product Status Enum
    public enum ProductStatus {
        ACTIVE, INACTIVE, SUSPENDED, DRAFT
    }
}
