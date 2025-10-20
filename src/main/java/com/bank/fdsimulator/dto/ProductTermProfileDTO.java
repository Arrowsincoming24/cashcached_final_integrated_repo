package com.bank.fdsimulator.dto;

import com.bank.fdsimulator.entity.ProductTermProfile.CompoundingFrequency;
import java.math.BigDecimal;

public class ProductTermProfileDTO {
    private Long id;
    private Long productId;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Integer minTermDays;
    private Integer maxTermDays;
    private CompoundingFrequency compoundingFrequency;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public void setProductId(Long productId) {
        this.productId = productId;
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
    
    public Integer getMinTermDays() {
        return minTermDays;
    }
    
    public void setMinTermDays(Integer minTermDays) {
        this.minTermDays = minTermDays;
    }
    
    public Integer getMaxTermDays() {
        return maxTermDays;
    }
    
    public void setMaxTermDays(Integer maxTermDays) {
        this.maxTermDays = maxTermDays;
    }
    
    public CompoundingFrequency getCompoundingFrequency() {
        return compoundingFrequency;
    }
    
    public void setCompoundingFrequency(CompoundingFrequency compoundingFrequency) {
        this.compoundingFrequency = compoundingFrequency;
    }
}
