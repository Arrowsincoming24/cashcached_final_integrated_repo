package com.bank.fdsimulator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    
    @GetMapping("/")
    public String home() {
        return "redirect:cashcached";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login-premium";
    }
    
    @GetMapping("/admin/dashboard")
    public String adminDashboard() {
        return "admin-modern";
    }
    
    @GetMapping("/admin/dashboard-old")
    public String adminDashboardOld() {
        return "admin-dashboard-new";
    }
    
    @GetMapping("/admin/products")
    public String adminProducts() {
        return "admin-product-management";
    }
    
    @GetMapping("/admin/users")
    public String adminUsers() {
        return "admin-user-management";
    }
    
    @GetMapping("/admin/time-travel")
    public String adminTimeTravel() {
        return "admin-time-travel";
    }
    
    @GetMapping("/admin/batch-processing")
    public String adminBatchProcessing() {
        return "admin-batch-processing";
    }
    
    @GetMapping("/customer/dashboard")
    public String customerDashboard() {
        return "customer-modern";
    }
    
    @GetMapping("/customer/dashboard-old")
    public String customerDashboardOld() {
        return "customer-dashboard";
    }
    
    @GetMapping("/register")
    public String register() {
        return "register-new";
    }
    
    @GetMapping("/cashcached")
    public String cashcached() {
        return "home-banking";
    }
    
    @GetMapping("/fd-calculator")
    public String fdCalculator() {
        return "fd-calculator-premium";
    }
}
