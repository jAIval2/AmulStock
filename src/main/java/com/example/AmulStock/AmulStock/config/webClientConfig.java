package com.example.AmulStock.AmulStock.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.CookieManager;

@Configuration
public class webClientConfig {
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Accept-Language", "en-US,en;q=0.9")
                .defaultHeader("Origin", "https://shop.amul.com")
                .defaultHeader("Referer", "https://shop.amul.com/")
                .build();
    }
    @Bean
    public CookieManager cookieManager() {
        return new CookieManager();
    }
}
