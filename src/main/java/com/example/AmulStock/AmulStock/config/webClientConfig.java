package com.example.AmulStock.AmulStock.config;

import com.example.AmulStock.AmulStock.utils.authManager;
import com.example.AmulStock.AmulStock.utils.cookieManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.CookieManager;

@Configuration
public class webClientConfig {

    @Bean
    public CookieManager javaCookieManager() {
        return new CookieManager();
    }

    @Bean
    public cookieManager amulCookieManager() {
        return new cookieManager(javaCookieManager());
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://shop.amul.com")
                .defaultHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .defaultHeader("Accept", "application/json, text/javascript, */*; q=0.01")
                .defaultHeader("Accept-Language", "en-US,en;q=0.9")
                .defaultHeader("Origin", "https://shop.amul.com")
                .defaultHeader("Referer", "https://shop.amul.com/")
                .build();
    }

    @Bean
    public authManager amulAuthManager(WebClient webClient, cookieManager cookieManager) {
        return new authManager(webClient, cookieManager);
    }
}