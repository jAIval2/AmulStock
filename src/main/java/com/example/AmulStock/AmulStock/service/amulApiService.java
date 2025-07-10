package com.example.AmulStock.AmulStock.service;
import com.example.AmulStock.AmulStock.utils.authManager;
import com.example.AmulStock.AmulStock.utils.cookieManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.CookieManager;
import java.util.Map;

@Slf4j
@Service
public class amulApiService {
    private final WebClient webClient;
    private final authManager authManager;
    private final cookieManager cookieManager;

    @Autowired
    public amulApiService(WebClient webClient,
                          authManager authManager,
                          cookieManager cookieManager) {
        this.webClient = webClient;
        this.authManager = authManager;
        this.cookieManager = cookieManager;
    }

    @PostConstruct
    public void init() {
        try {
            log.info("Initializing Amul session...");
            authManager.initSession();
            log.info("Successfully initialized Amul session");
        } catch (Exception e) {
            log.error("Failed to initialize Amul session", e);
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getAmulProducts() {
        String tidHeader = authManager.generateTidHeader();
        String cookieHeader = cookieManager.getCookiesAsString();

        return webClient.get()
                .uri("/api/1/entity/ms.products?fields[name]=1&fields[brand]=1&fields[categories]=1&fields[collections]=1&fields[alias]=1&fields[sku]=1&fields[price]=1&fields[compare_price]=1&fields[original_price]=1&fields[images]=1&fields[metafields]=1&fields[discounts]=1&fields[catalog_only]=1&fields[is_catalog]=1&fields[seller]=1&fields[available]=1&fields[inventory_quantity]=1&fields[net_quantity]=1&fields[num_reviews]=1&fields[avg_rating]=1&fields[inventory_low_stock_quantity]=1&fields[inventory_allow_out_of_stock]=1&fields[default_variant]=1&fields[variants]=1&fields[lp_seller_ids]=1&filters[0][field]=categories&filters[0][value][0]=protein&filters[0][operator]=in&filters[0][original]=1&facets=true&facetgroup=default_category_facet&limit=24&total=1&start=0&cdc=1m")
                .header("Cookie", cookieHeader)
                .header("tid", tidHeader)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .block();
    }

}