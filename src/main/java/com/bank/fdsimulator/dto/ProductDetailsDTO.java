package com.bank.fdsimulator.dto;

import com.bank.fdsimulator.entity.FdProduct.ProductStatus;
import java.math.BigDecimal;
import java.util.List;

public class ProductDetailsDTO {
    private Long id;
    private String productCode;
    private String productName;
    private String description;
    private ProductStatus status;
    private String currency;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Integer minTenureMonths;
    private Integer maxTenureMonths;
    private BigDecimal interestRate;
    private List<ProductTermProfileDTO> termProfiles;
    private List<RateMatrixDTO> rateMatrices;
    private List<BusinessRuleDTO> businessRules;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getProductCode() {
        return productCode;
    }
    
    public void setProductCode(String productCode) {
        this.productCode = productCode;
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
    
    public List<ProductTermProfileDTO> getTermProfiles() {
        return termProfiles;
    }
    
    public void setTermProfiles(List<ProductTermProfileDTO> termProfiles) {
        this.termProfiles = termProfiles;
    }
    
    public List<RateMatrixDTO> getRateMatrices() {
        return rateMatrices;
    }
    
    public void setRateMatrices(List<RateMatrixDTO> rateMatrices) {
        this.rateMatrices = rateMatrices;
    }
    
    public List<BusinessRuleDTO> getBusinessRules() {
        return businessRules;
    }
    
    public void setBusinessRules(List<BusinessRuleDTO> businessRules) {
        this.businessRules = businessRules;
    }
}
