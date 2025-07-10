package com.example.AmulStock.AmulStock.utils;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.security.MessageDigest;
import java.util.List;

@Component
public class authManager {
    private final WebClient webClient;
    private final cookieManager cookieManager;
    private String sessionId;

    public authManager(WebClient webClient, cookieManager cookieManager) {
        this.webClient = webClient;
        this.cookieManager = cookieManager;
    }
    public void initSession(){
        List<String> setCookies = webClient.get()
                .uri("https://shop.amul.com/en/browse/protein")
                .exchangeToMono(resp ->{
                    List<String> cookies = resp.headers().header("Set-Cookie");
                    return Mono.just(cookies);
                }).block();
        cookieManager.storeCookies(setCookies);

        String infoJs = webClient.get()
                .uri("https://shop.amul.com/user/info.js?_v=" + System.currentTimeMillis())
                .header("Cookie", cookieManager.getCookiesAsString())
                .retrieve().bodyToMono(String.class).block();
        this.sessionId = parseTid(infoJs);
    }
    public String generateTidHeader() {
        String storeId = "62fa94df8c13af2e242eba16";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf((int)(Math.random() * 1000));
        String input = storeId + ":" + timestamp + ":" + random + ":" + sessionId;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) hex.append(String.format("%02x", b));
            return timestamp + ":" + random + ":" + hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate TID", e);
        }
    }

    private String parseTid(String infoJs) {
        // crude parsing: session = {..."tid":"<sessionId>"...}
        int idx = infoJs.indexOf("\"tid\":\"");
        if (idx == -1) throw new RuntimeException("tid not found");
        int start = idx + 7;
        int end = infoJs.indexOf("\"", start);
        return infoJs.substring(start, end);
    }
}

