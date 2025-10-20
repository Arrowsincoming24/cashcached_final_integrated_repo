package com.bank.fdsimulator.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "business_rule")
public class BusinessRule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private FdProduct product;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rule_type_id", nullable = false)
    private BusinessRuleType ruleType;
    
    @Column(name = "rule_value", columnDefinition = "TEXT", nullable = false)
    private String ruleValue;
    
    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;
    
    @Column(name = "effective_to")
    private LocalDate effectiveTo;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public BusinessRule() {}
    
    public BusinessRule(FdProduct product, BusinessRuleType ruleType, String ruleValue,
                       LocalDate effectiveFrom) {
        this.product = product;
        this.ruleType = ruleType;
        this.ruleValue = ruleValue;
        this.effectiveFrom = effectiveFrom;
    }
    
    // Business logic
    public boolean isActiveOn(LocalDate date) {
        boolean afterEffective = !date.isBefore(effectiveFrom);
        boolean beforeExpiry = effectiveTo == null || !date.isAfter(effectiveTo);
        return afterEffective && beforeExpiry;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public FdProduct getProduct() {
        return product;
    }
    
    public void setProduct(FdProduct product) {
        this.product = product;
    }
    
    public BusinessRuleType getRuleType() {
        return ruleType;
    }
    
    public void setRuleType(BusinessRuleType ruleType) {
        this.ruleType = ruleType;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
