package com.bank.fdsimulator.dto;

import com.bank.fdsimulator.entity.FdProduct.ProductStatus;

public class ProductSummaryDTO {
    private Long id;
    private String productCode;
    private String productName;
    private String description;
    private ProductStatus status;
    private String currency;
    
    public ProductSummaryDTO() {}
    
    public ProductSummaryDTO(Long id, String productCode, String productName, String description, 
                           ProductStatus status, String currency) {
        this.id = id;
        this.productCode = productCode;
        this.productName = productName;
        this.description = description;
        this.status = status;
        this.currency = currency;
    }
    
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
}
