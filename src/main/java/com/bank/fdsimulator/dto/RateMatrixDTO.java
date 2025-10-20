package com.bank.fdsimulator.dto;

import com.bank.fdsimulator.entity.RateMatrix.CustomerType;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RateMatrixDTO {
    private Long id;
    private Long productId;
    private CustomerType customerType;
    private Integer termFromDays;
    private Integer termToDays;
    private BigDecimal interestRate;
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
    
    public CustomerType getCustomerType() {
        return customerType;
    }
    
    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }
    
    public Integer getTermFromDays() {
        return termFromDays;
    }
    
    public void setTermFromDays(Integer termFromDays) {
        this.termFromDays = termFromDays;
    }
    
    public Integer getTermToDays() {
        return termToDays;
    }
    
    public void setTermToDays(Integer termToDays) {
        this.termToDays = termToDays;
    }
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
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
