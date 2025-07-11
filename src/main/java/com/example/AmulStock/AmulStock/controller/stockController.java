package com.example.AmulStock.AmulStock.controller;

import com.example.AmulStock.AmulStock.dto.amulProductsDTO;
import com.example.AmulStock.AmulStock.service.amulApiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class stockController {
    private final amulApiService amulApiService;

    public stockController(amulApiService amulApiService) {
        this.amulApiService = amulApiService;
    }

        @GetMapping("/api/stock/amul")
    public ResponseEntity<?> getAmulStock() {
        try {
            return ResponseEntity.ok(amulApiService.getAmulProducts());
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch Amul products: " + e.getMessage()));
        }
    }
    
    @GetMapping("/hehe")
    public ResponseEntity<?> getAmulProducts() {
        try {
            List<amulProductsDTO> products = amulApiService.getAmulProductsDTO();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch Amul products: " + e.getMessage()));
        }
    }
}