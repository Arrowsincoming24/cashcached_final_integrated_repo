package com.bank.fdsimulator.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_term_profile")
public class ProductTermProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private FdProduct product;
    
    @Column(name = "min_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal minAmount;
    
    @Column(name = "max_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal maxAmount;
    
    @Column(name = "min_term_days", nullable = false)
    private Integer minTermDays;
    
    @Column(name = "max_term_days", nullable = false)
    private Integer maxTermDays;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "compounding_frequency", length = 20)
    private CompoundingFrequency compoundingFrequency;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public enum CompoundingFrequency {
        MONTHLY, QUARTERLY, HALF_YEARLY, YEARLY
    }
    
    // Constructors
    public ProductTermProfile() {}
    
    public ProductTermProfile(FdProduct product, BigDecimal minAmount, BigDecimal maxAmount,
                            Integer minTermDays, Integer maxTermDays, CompoundingFrequency compoundingFrequency) {
        this.product = product;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.minTermDays = minTermDays;
        this.maxTermDays = maxTermDays;
        this.compoundingFrequency = compoundingFrequency;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
