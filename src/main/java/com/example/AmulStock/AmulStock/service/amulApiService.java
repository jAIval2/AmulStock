package com.example.AmulStock.AmulStock.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class amulApiService {
    public Map<String,Object> getdumstock(){
        return Map.of("Product","Amul Protien Bars",
                "available","True",
                "stock",3);
    }
}
