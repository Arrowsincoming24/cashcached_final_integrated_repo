package com.bank.fdsimulator.dto;

import java.time.LocalDate;

public class BusinessRuleDTO {
    private Long id;
    private Long productId;
    private Long ruleTypeId;
    private String ruleTypeName;
    private String ruleTypeCode;
    private String ruleValue;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    
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
    
    public Long getRuleTypeId() {
        return ruleTypeId;
    }
    
    public void setRuleTypeId(Long ruleTypeId) {
        this.ruleTypeId = ruleTypeId;
    }
    
    public String getRuleTypeName() {
        return ruleTypeName;
    }
    
    public void setRuleTypeName(String ruleTypeName) {
        this.ruleTypeName = ruleTypeName;
    }
    
    public String getRuleTypeCode() {
        return ruleTypeCode;
    }
    
    public void setRuleTypeCode(String ruleTypeCode) {
        this.ruleTypeCode = ruleTypeCode;
    }
    
    public String getRuleValue() {
        return ruleValue;
    }
    
    public void setRuleValue(String ruleValue) {
        this.ruleValue = ruleValue;
    }
    
    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }
    
    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }
    
    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }
    
    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }
}
