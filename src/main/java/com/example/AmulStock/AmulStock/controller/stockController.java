package com.example.AmulStock.AmulStock.controller;
import com.example.AmulStock.AmulStock.service.amulApiService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class stockController {
    private final amulApiService amulApiService;
    public stockController(amulApiService amulApiService) {
        this.amulApiService = amulApiService;
    }
    @GetMapping("api/stock")
    public Map<String,Object> getStock(){
        return amulApiService.getdumstock();
    }
}