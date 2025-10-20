package com.bank.fdsimulator.config;

import com.bank.fdsimulator.entity.*;
import com.bank.fdsimulator.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private FdProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private BusinessRuleTypeRepository businessRuleTypeRepository;
    
    @Override
    public void run(String... args) throws Exception {
        // Initialize default admin user
        initializeAdminUser();
        
        // Initialize business rule types
        initializeBusinessRuleTypes();
        
        // Check if products already exist
        if (productRepository.count() == 0) {
            initializeProducts();
        }
    }
    
    private void initializeAdminUser() {
        // Check if admin already exists
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@cashcached.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(Role.ADMIN);
            admin.setPreferredCurrency(Currency.USD);
            admin.setPreferredLanguage(Language.EN);
            admin.setEnabled(true);
            userRepository.save(admin);
            
            System.out.println("\n" + "=".repeat(60));
            System.out.println("✓ DEFAULT ADMIN ACCOUNT CREATED");
            System.out.println("=".repeat(60));
            System.out.println("Username: admin");
            System.out.println("Password: admin123");
            System.out.println("Email: admin@cashcached.com");
            System.out.println("=".repeat(60) + "\n");
        }
    }
    
    private void initializeProducts() {
        // Product 1: CashCached Short Term
        FdProduct product1 = new FdProduct(
            "CashCached Short Term",
            "Perfect for short-term savings with flexible tenure options",
            new BigDecimal("5000"),
            new BigDecimal("100000"),
            3,
            60,
            new BigDecimal("7.0")
        );
        product1.setMinAmountUSD(new BigDecimal("60"));
        product1.setMaxAmountUSD(new BigDecimal("12000"));
        product1.setMinAmountINR(new BigDecimal("5000"));
        product1.setMaxAmountINR(new BigDecimal("1000000"));
        product1.setMinAmountKWD(new BigDecimal("18"));
        product1.setMaxAmountKWD(new BigDecimal("3700"));
        productRepository.save(product1);
        
        // Product 2: CashCached Regular
        FdProduct product2 = new FdProduct(
            "CashCached Regular",
            "Standard fixed deposit with competitive interest rates",
            new BigDecimal("10000"),
            new BigDecimal("500000"),
            6,
            24,
            new BigDecimal("6.5")
        );
        product2.setMinAmountUSD(new BigDecimal("120"));
        product2.setMaxAmountUSD(new BigDecimal("6000"));
        product2.setMinAmountINR(new BigDecimal("10000"));
        product2.setMaxAmountINR(new BigDecimal("500000"));
        product2.setMinAmountKWD(new BigDecimal("37"));
        product2.setMaxAmountKWD(new BigDecimal("1850"));
        productRepository.save(product2);
        
        // Product 3: CashCached Senior
        FdProduct product3 = new FdProduct(
            "CashCached Senior",
            "Exclusive FD scheme for senior citizens with higher returns",
            new BigDecimal("25000"),
            new BigDecimal("1000000"),
            12,
            60,
            new BigDecimal("7.5")
        );
        product3.setMinAmountUSD(new BigDecimal("300"));
        product3.setMaxAmountUSD(new BigDecimal("12000"));
        product3.setMinAmountINR(new BigDecimal("25000"));
        product3.setMaxAmountINR(new BigDecimal("1000000"));
        product3.setMinAmountKWD(new BigDecimal("93"));
        product3.setMaxAmountKWD(new BigDecimal("3700"));
        productRepository.save(product3);
        
        // Product 4: CashCached Tax Saver
        FdProduct product4 = new FdProduct(
            "CashCached Tax Saver",
            "5-year lock-in period with tax benefits under Section 80C",
            new BigDecimal("10000"),
            new BigDecimal("150000"),
            60,
            60,
            new BigDecimal("6.75")
        );
        product4.setMinAmountUSD(new BigDecimal("120"));
        product4.setMaxAmountUSD(new BigDecimal("1800"));
        product4.setMinAmountINR(new BigDecimal("10000"));
        product4.setMaxAmountINR(new BigDecimal("150000"));
        product4.setMinAmountKWD(new BigDecimal("37"));
        product4.setMaxAmountKWD(new BigDecimal("555"));
        productRepository.save(product4);
        
        // Product 5: CashCached Flexi
        FdProduct product5 = new FdProduct(
            "CashCached Flexi",
            "Flexible deposit with partial withdrawal facility",
            new BigDecimal("50000"),
            new BigDecimal("2000000"),
            12,
            36,
            new BigDecimal("6.25")
        );
        product5.setMinAmountUSD(new BigDecimal("600"));
        product5.setMaxAmountUSD(new BigDecimal("24000"));
        product5.setMinAmountINR(new BigDecimal("50000"));
        product5.setMaxAmountINR(new BigDecimal("2000000"));
        product5.setMinAmountKWD(new BigDecimal("185"));
        product5.setMaxAmountKWD(new BigDecimal("7400"));
        productRepository.save(product5);
        
        // Product 6: CashCached Premium
        FdProduct product6 = new FdProduct(
            "CashCached Premium",
            "Premium FD for high-value deposits with attractive rates",
            new BigDecimal("500000"),
            new BigDecimal("10000000"),
            12,
            60,
            new BigDecimal("7.25")
        );
        product6.setMinAmountUSD(new BigDecimal("6000"));
        product6.setMaxAmountUSD(new BigDecimal("120000"));
        product6.setMinAmountINR(new BigDecimal("500000"));
        product6.setMaxAmountINR(new BigDecimal("10000000"));
        product6.setMinAmountKWD(new BigDecimal("1850"));
        product6.setMaxAmountKWD(new BigDecimal("37000"));
        productRepository.save(product6);
        
        // Product 7: CashCached Monthly Income
        FdProduct product7 = new FdProduct(
            "CashCached Monthly Income",
            "Earn monthly interest payouts for regular income",
            new BigDecimal("100000"),
            new BigDecimal("5000000"),
            12,
            60,
            new BigDecimal("6.85")
        );
        product7.setMinAmountUSD(new BigDecimal("1200"));
        product7.setMaxAmountUSD(new BigDecimal("60000"));
        product7.setMinAmountINR(new BigDecimal("100000"));
        product7.setMaxAmountINR(new BigDecimal("5000000"));
        product7.setMinAmountKWD(new BigDecimal("370"));
        product7.setMaxAmountKWD(new BigDecimal("18500"));
        productRepository.save(product7);
        
        // Product 8: CashCached Youth
        FdProduct product8 = new FdProduct(
            "CashCached Youth",
            "Special FD scheme for youth (18-30 years) with bonus rates",
            new BigDecimal("5000"),
            new BigDecimal("200000"),
            6,
            36,
            new BigDecimal("6.0")
        );
        product8.setMinAmountUSD(new BigDecimal("60"));
        product8.setMaxAmountUSD(new BigDecimal("2400"));
        product8.setMinAmountINR(new BigDecimal("5000"));
        product8.setMaxAmountINR(new BigDecimal("200000"));
        product8.setMinAmountKWD(new BigDecimal("18"));
        product8.setMaxAmountKWD(new BigDecimal("740"));
        productRepository.save(product8);
        
        // Product 9: CashCached Corporate
        FdProduct product9 = new FdProduct(
            "CashCached Corporate",
            "Bulk deposits for corporate entities with premium rates",
            new BigDecimal("1000000"),
            new BigDecimal("50000000"),
            12,
            60,
            new BigDecimal("7.75")
        );
        product9.setMinAmountUSD(new BigDecimal("12000"));
        product9.setMaxAmountUSD(new BigDecimal("600000"));
        product9.setMinAmountINR(new BigDecimal("1000000"));
        product9.setMaxAmountINR(new BigDecimal("50000000"));
        product9.setMinAmountKWD(new BigDecimal("3700"));
        product9.setMaxAmountKWD(new BigDecimal("185000"));
        productRepository.save(product9);
        
        // Product 10: CashCached Cumulative
        FdProduct product10 = new FdProduct(
            "CashCached Cumulative",
            "Interest compounded quarterly for maximum returns",
            new BigDecimal("25000"),
            new BigDecimal("1000000"),
            12,
            60,
            new BigDecimal("7.0")
        );
        product10.setMinAmountUSD(new BigDecimal("300"));
        product10.setMaxAmountUSD(new BigDecimal("12000"));
        product10.setMinAmountINR(new BigDecimal("25000"));
        product10.setMaxAmountINR(new BigDecimal("1000000"));
        product10.setMinAmountKWD(new BigDecimal("93"));
        product10.setMaxAmountKWD(new BigDecimal("3700"));
        productRepository.save(product10);
        
        System.out.println("✓ Initialized 10 CashCached products successfully");
    }
    
    private void initializeBusinessRuleTypes() {
        if (businessRuleTypeRepository.count() == 0) {
            BusinessRuleType minBalRule = new BusinessRuleType(
                "MIN_BAL", 
                "Minimum Balance Rule", 
                "Defines the minimum balance required"
            );
            businessRuleTypeRepository.save(minBalRule);
            
            BusinessRuleType earlyWdRule = new BusinessRuleType(
                "EARLY_WD", 
                "Early Withdrawal Rule", 
                "Penalty for premature withdrawal"
            );
            businessRuleTypeRepository.save(earlyWdRule);
            
            BusinessRuleType maxInvRule = new BusinessRuleType(
                "MAX_INV", 
                "Maximum Investment Rule", 
                "Defines the maximum amount allowed per customer"
            );
            businessRuleTypeRepository.save(maxInvRule);
            
            BusinessRuleType compoundingRule = new BusinessRuleType(
                "COMPOUNDING", 
                "Compounding Frequency Rule", 
                "Defines how interest is compounded"
            );
            businessRuleTypeRepository.save(compoundingRule);
            
            BusinessRuleType taxRule = new BusinessRuleType(
                "TAX_BENEFIT", 
                "Tax Benefit Rule", 
                "Defines tax benefits applicable"
            );
            businessRuleTypeRepository.save(taxRule);
            
            System.out.println("✓ Initialized business rule types successfully");
        }
    }
}
