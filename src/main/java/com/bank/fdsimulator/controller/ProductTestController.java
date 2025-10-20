package com.bank.fdsimulator.controller;

import com.bank.fdsimulator.entity.FdProduct;
import com.bank.fdsimulator.service.FdProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*")
public class ProductTestController {
    
    @Autowired
    private FdProductService productService;
    
    @GetMapping("/products-test")
    public ResponseEntity<List<FdProduct>> testGetProducts() {
        List<FdProduct> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
    
    @GetMapping("/products-count")
    public ResponseEntity<String> testProductCount() {
        try {
            List<FdProduct> products = productService.getAllProducts();
            return ResponseEntity.ok("Product count: " + products.size());
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }
}
