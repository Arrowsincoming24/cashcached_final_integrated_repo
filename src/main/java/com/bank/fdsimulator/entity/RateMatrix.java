package com.bank.fdsimulator.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rate_matrix")
public class RateMatrix {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private FdProduct product;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", length = 20, nullable = false)
    private CustomerType customerType;
    
    @Column(name = "term_from_days", nullable = false)
    private Integer termFromDays;
    
    @Column(name = "term_to_days", nullable = false)
    private Integer termToDays;
    
    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;
    
    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;
    
    @Column(name = "effective_to")
    private LocalDate effectiveTo;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public enum CustomerType {
        RETAIL, CORPORATE, SENIOR_CITIZEN, STAFF
    }
    
    // Constructors
    public RateMatrix() {}
    
    public RateMatrix(FdProduct product, CustomerType customerType, Integer termFromDays,
                     Integer termToDays, BigDecimal interestRate, LocalDate effectiveFrom) {
        this.product = product;
        this.customerType = customerType;
        this.termFromDays = termFromDays;
        this.termToDays = termToDays;
        this.interestRate = interestRate;
        this.effectiveFrom = effectiveFrom;
    }
    
    // Business logic
    public boolean isApplicableFor(Integer termDays, LocalDate date) {
        boolean termMatch = termDays >= termFromDays && termDays <= termToDays;
        boolean dateMatch = !date.isBefore(effectiveFrom) && 
                          (effectiveTo == null || !date.isAfter(effectiveTo));
        return termMatch && dateMatch;
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
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
