package com.bank.fdsimulator.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_audit_log")
public class ProductAuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private FdProduct product;
    
    @Column(name = "created_by", length = 100, nullable = false)
    private String createdBy;
    
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = 20, nullable = false)
    private AuditAction action;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    public enum AuditAction {
        CREATE, UPDATE, DELETE, STATUS_CHANGE, RATE_UPDATE, RULE_UPDATE
    }
    
    // Constructors
    public ProductAuditLog() {}
    
    public ProductAuditLog(FdProduct product, String createdBy, String details, AuditAction action) {
        this.product = product;
        this.createdBy = createdBy;
        this.details = details;
        this.action = action;
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
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    public AuditAction getAction() {
        return action;
    }
    
    public void setAction(AuditAction action) {
        this.action = action;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
