package com.bank.fdsimulator.service;

import com.bank.fdsimulator.dto.*;
import com.bank.fdsimulator.entity.*;
import com.bank.fdsimulator.entity.FdProduct.ProductStatus;
import com.bank.fdsimulator.entity.ProductAuditLog.AuditAction;
import com.bank.fdsimulator.entity.RateMatrix.CustomerType;
import com.bank.fdsimulator.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductManagementService {
    
    @Autowired
    private FdProductRepository productRepository;
    
    @Autowired
    private ProductTermProfileRepository termProfileRepository;
    
    @Autowired
    private RateMatrixRepository rateMatrixRepository;
    
    @Autowired
    private BusinessRuleRepository businessRuleRepository;
    
    @Autowired
    private BusinessRuleTypeRepository ruleTypeRepository;
    
    @Autowired
    private ProductAuditLogRepository auditLogRepository;
    
    // ==================== Product Management ====================
    
    public List<ProductSummaryDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(this::convertToSummaryDTO)
            .collect(Collectors.toList());
    }
    
    public List<ProductSummaryDTO> getActiveProducts() {
        return productRepository.findByIsActive(true).stream()
            .map(this::convertToSummaryDTO)
            .collect(Collectors.toList());
    }
    
    public ProductDetailsDTO getProductDetails(Long id) {
        FdProduct product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        return convertToDetailsDTO(product);
    }
    
    public FdProduct createProduct(FdProduct product, String createdBy) {
        product.setStatus(ProductStatus.DRAFT);
        FdProduct saved = productRepository.save(product);
        
        // Create audit log
        ProductAuditLog auditLog = new ProductAuditLog(saved, createdBy, 
            "Product created: " + product.getProductName(), AuditAction.CREATE);
        auditLogRepository.save(auditLog);
        
        return saved;
    }
    
    public FdProduct updateProduct(Long id, FdProduct productData, String updatedBy) {
        FdProduct existing = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        existing.setProductName(productData.getProductName());
        existing.setDescription(productData.getDescription());
        existing.setCurrency(productData.getCurrency());
        existing.setMinAmount(productData.getMinAmount());
        existing.setMaxAmount(productData.getMaxAmount());
        existing.setMinTenureMonths(productData.getMinTenureMonths());
        existing.setMaxTenureMonths(productData.getMaxTenureMonths());
        existing.setInterestRate(productData.getInterestRate());
        
        FdProduct updated = productRepository.save(existing);
        
        // Create audit log
        ProductAuditLog auditLog = new ProductAuditLog(updated, updatedBy,
            "Product updated: " + existing.getProductName(), AuditAction.UPDATE);
        auditLogRepository.save(auditLog);
        
        return updated;
    }
    
    public void deleteProduct(Long id, String deletedBy) {
        FdProduct product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        // Create audit log before deletion
        ProductAuditLog auditLog = new ProductAuditLog(product, deletedBy,
            "Product deleted: " + product.getProductName(), AuditAction.DELETE);
        auditLogRepository.save(auditLog);
        
        productRepository.deleteById(id);
    }
    
    public FdProduct changeStatus(Long id, ProductStatus status, String updatedBy) {
        FdProduct product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
        
        ProductStatus oldStatus = product.getStatus();
        product.setStatus(status);
        FdProduct updated = productRepository.save(product);
        
        // Create audit log
        ProductAuditLog auditLog = new ProductAuditLog(updated, updatedBy,
            "Status changed from " + oldStatus + " to " + status, AuditAction.STATUS_CHANGE);
        auditLogRepository.save(auditLog);
        
        return updated;
    }
    
    // ==================== Term Profile Management ====================
    
    public ProductTermProfileDTO addTermProfile(Long productId, ProductTermProfileDTO dto) {
        FdProduct product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        ProductTermProfile profile = new ProductTermProfile();
        profile.setProduct(product);
        profile.setMinAmount(dto.getMinAmount());
        profile.setMaxAmount(dto.getMaxAmount());
        profile.setMinTermDays(dto.getMinTermDays());
        profile.setMaxTermDays(dto.getMaxTermDays());
        profile.setCompoundingFrequency(dto.getCompoundingFrequency());
        
        ProductTermProfile saved = termProfileRepository.save(profile);
        return convertToTermProfileDTO(saved);
    }
    
    public List<ProductTermProfileDTO> getTermProfiles(Long productId) {
        return termProfileRepository.findByProductId(productId).stream()
            .map(this::convertToTermProfileDTO)
            .collect(Collectors.toList());
    }
    
    public ProductTermProfileDTO updateTermProfile(Long profileId, ProductTermProfileDTO dto) {
        ProductTermProfile profile = termProfileRepository.findById(profileId)
            .orElseThrow(() -> new RuntimeException("Term profile not found with id: " + profileId));
        
        profile.setMinAmount(dto.getMinAmount());
        profile.setMaxAmount(dto.getMaxAmount());
        profile.setMinTermDays(dto.getMinTermDays());
        profile.setMaxTermDays(dto.getMaxTermDays());
        profile.setCompoundingFrequency(dto.getCompoundingFrequency());
        
        ProductTermProfile updated = termProfileRepository.save(profile);
        return convertToTermProfileDTO(updated);
    }
    
    public void deleteTermProfile(Long profileId) {
        termProfileRepository.deleteById(profileId);
    }
    
    // ==================== Rate Matrix Management ====================
    
    public RateMatrixDTO addRate(Long productId, RateMatrixDTO dto) {
        FdProduct product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        RateMatrix rate = new RateMatrix();
        rate.setProduct(product);
        rate.setCustomerType(dto.getCustomerType());
        rate.setTermFromDays(dto.getTermFromDays());
        rate.setTermToDays(dto.getTermToDays());
        rate.setInterestRate(dto.getInterestRate());
        rate.setEffectiveFrom(dto.getEffectiveFrom() != null ? dto.getEffectiveFrom() : LocalDate.now());
        rate.setEffectiveTo(dto.getEffectiveTo());
        
        RateMatrix saved = rateMatrixRepository.save(rate);
        
        // Create audit log
        ProductAuditLog auditLog = new ProductAuditLog(product, "system",
            "Rate added for " + dto.getCustomerType() + ": " + dto.getInterestRate() + "%", 
            AuditAction.RATE_UPDATE);
        auditLogRepository.save(auditLog);
        
        return convertToRateMatrixDTO(saved);
    }
    
    public List<RateMatrixDTO> getRates(Long productId, String customerType) {
        List<RateMatrix> rates;
        if (customerType != null && !customerType.isEmpty()) {
            rates = rateMatrixRepository.findByProductIdAndCustomerType(
                productId, CustomerType.valueOf(customerType));
        } else {
            rates = rateMatrixRepository.findByProductId(productId);
        }
        return rates.stream()
            .map(this::convertToRateMatrixDTO)
            .collect(Collectors.toList());
    }
    
    public RateMatrixDTO updateRate(Long rateId, RateMatrixDTO dto) {
        RateMatrix rate = rateMatrixRepository.findById(rateId)
            .orElseThrow(() -> new RuntimeException("Rate not found with id: " + rateId));
        
        rate.setCustomerType(dto.getCustomerType());
        rate.setTermFromDays(dto.getTermFromDays());
        rate.setTermToDays(dto.getTermToDays());
        rate.setInterestRate(dto.getInterestRate());
        rate.setEffectiveFrom(dto.getEffectiveFrom());
        rate.setEffectiveTo(dto.getEffectiveTo());
        
        RateMatrix updated = rateMatrixRepository.save(rate);
        return convertToRateMatrixDTO(updated);
    }
    
    public void deleteRate(Long rateId) {
        rateMatrixRepository.deleteById(rateId);
    }
    
    // ==================== Business Rule Management ====================
    
    public BusinessRuleDTO addRule(Long productId, BusinessRuleDTO dto) {
        FdProduct product = productRepository.findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));
        
        BusinessRuleType ruleType = ruleTypeRepository.findById(dto.getRuleTypeId())
            .orElseThrow(() -> new RuntimeException("Rule type not found with id: " + dto.getRuleTypeId()));
        
        BusinessRule rule = new BusinessRule();
        rule.setProduct(product);
        rule.setRuleType(ruleType);
        rule.setRuleValue(dto.getRuleValue());
        rule.setEffectiveFrom(dto.getEffectiveFrom() != null ? dto.getEffectiveFrom() : LocalDate.now());
        rule.setEffectiveTo(dto.getEffectiveTo());
        
        BusinessRule saved = businessRuleRepository.save(rule);
        
        // Create audit log
        ProductAuditLog auditLog = new ProductAuditLog(product, "system",
            "Business rule added: " + ruleType.getName(), AuditAction.RULE_UPDATE);
        auditLogRepository.save(auditLog);
        
        return convertToBusinessRuleDTO(saved);
    }
    
    public List<BusinessRuleDTO> getRules(Long productId) {
        return businessRuleRepository.findByProductId(productId).stream()
            .map(this::convertToBusinessRuleDTO)
            .collect(Collectors.toList());
    }
    
    public BusinessRuleDTO updateRule(Long ruleId, BusinessRuleDTO dto) {
        BusinessRule rule = businessRuleRepository.findById(ruleId)
            .orElseThrow(() -> new RuntimeException("Rule not found with id: " + ruleId));
        
        if (dto.getRuleTypeId() != null) {
            BusinessRuleType ruleType = ruleTypeRepository.findById(dto.getRuleTypeId())
                .orElseThrow(() -> new RuntimeException("Rule type not found"));
            rule.setRuleType(ruleType);
        }
        
        rule.setRuleValue(dto.getRuleValue());
        rule.setEffectiveFrom(dto.getEffectiveFrom());
        rule.setEffectiveTo(dto.getEffectiveTo());
        
        BusinessRule updated = businessRuleRepository.save(rule);
        return convertToBusinessRuleDTO(updated);
    }
    
    public void deleteRule(Long ruleId) {
        businessRuleRepository.deleteById(ruleId);
    }
    
    public List<BusinessRuleType> getAllRuleTypes() {
        return ruleTypeRepository.findAll();
    }
    
    // ==================== Conversion Methods ====================
    
    private ProductSummaryDTO convertToSummaryDTO(FdProduct product) {
        return new ProductSummaryDTO(
            product.getId(),
            product.getProductCode(),
            product.getProductName(),
            product.getDescription(),
            product.getStatus(),
            product.getCurrency()
        );
    }
    
    private ProductDetailsDTO convertToDetailsDTO(FdProduct product) {
        ProductDetailsDTO dto = new ProductDetailsDTO();
        dto.setId(product.getId());
        dto.setProductCode(product.getProductCode());
        dto.setProductName(product.getProductName());
        dto.setDescription(product.getDescription());
        dto.setStatus(product.getStatus());
        dto.setCurrency(product.getCurrency());
        dto.setMinAmount(product.getMinAmount());
        dto.setMaxAmount(product.getMaxAmount());
        dto.setMinTenureMonths(product.getMinTenureMonths());
        dto.setMaxTenureMonths(product.getMaxTenureMonths());
        dto.setInterestRate(product.getInterestRate());
        
        // Load related data
        dto.setTermProfiles(getTermProfiles(product.getId()));
        dto.setRateMatrices(getRates(product.getId(), null));
        dto.setBusinessRules(getRules(product.getId()));
        
        return dto;
    }
    
    private ProductTermProfileDTO convertToTermProfileDTO(ProductTermProfile profile) {
        ProductTermProfileDTO dto = new ProductTermProfileDTO();
        dto.setId(profile.getId());
        dto.setProductId(profile.getProduct().getId());
        dto.setMinAmount(profile.getMinAmount());
        dto.setMaxAmount(profile.getMaxAmount());
        dto.setMinTermDays(profile.getMinTermDays());
        dto.setMaxTermDays(profile.getMaxTermDays());
        dto.setCompoundingFrequency(profile.getCompoundingFrequency());
        return dto;
    }
    
    private RateMatrixDTO convertToRateMatrixDTO(RateMatrix rate) {
        RateMatrixDTO dto = new RateMatrixDTO();
        dto.setId(rate.getId());
        dto.setProductId(rate.getProduct().getId());
        dto.setCustomerType(rate.getCustomerType());
        dto.setTermFromDays(rate.getTermFromDays());
        dto.setTermToDays(rate.getTermToDays());
        dto.setInterestRate(rate.getInterestRate());
        dto.setEffectiveFrom(rate.getEffectiveFrom());
        dto.setEffectiveTo(rate.getEffectiveTo());
        return dto;
    }
    
    private BusinessRuleDTO convertToBusinessRuleDTO(BusinessRule rule) {
        BusinessRuleDTO dto = new BusinessRuleDTO();
        dto.setId(rule.getId());
        dto.setProductId(rule.getProduct().getId());
        dto.setRuleTypeId(rule.getRuleType().getId());
        dto.setRuleTypeName(rule.getRuleType().getName());
        dto.setRuleTypeCode(rule.getRuleType().getCode());
        dto.setRuleValue(rule.getRuleValue());
        dto.setEffectiveFrom(rule.getEffectiveFrom());
        dto.setEffectiveTo(rule.getEffectiveTo());
        return dto;
    }
}
