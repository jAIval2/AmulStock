package com.example.AmulStock.AmulStock.service;

import com.example.AmulStock.AmulStock.dto.amulProductsDTO;
import com.example.AmulStock.AmulStock.utils.authManager;
import com.example.AmulStock.AmulStock.utils.cookieManager;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    public List<amulProductsDTO> getAmulProductsDTO() {
        try {
            Map<String, Object> response = getAmulProducts();
            String fileBaseUrl = (String) response.get("fileBaseUrl");
            List<Map<String, Object>> products = (List<Map<String, Object>>) response.get("data");
            
            if (products == null) {
                log.error("No products found in the API response");
                return List.of();
            }
            
            return products.stream()
                .map(product -> {
                    try {
                        amulProductsDTO dto = new amulProductsDTO();
                        dto.setId((String) product.get("_id"));
                        dto.setName((String) product.get("name"));
                        dto.setAlias((String) product.get("alias"));
                        
                        // Safely handle numeric values that might be null
                        dto.setPrice(product.get("price") != null ? ((Number) product.get("price")).intValue() : 0);
                        dto.setComparePrice(product.get("compare_price") != null ? ((Number) product.get("compare_price")).intValue() : 0);
                        dto.setInventoryQuantity(product.get("inventory_quantity") != null ? ((Number) product.get("inventory_quantity")).intValue() : 0);
                        dto.setLowStockQuantity(product.get("inventory_low_stock_quantity") != null ? ((Number) product.get("inventory_low_stock_quantity")).intValue() : 0);
                        
                        // Handle available flag safely
                        Object available = product.get("available");
                        if (available instanceof Integer) {
                            dto.setAvailable((Integer) available == 1);
                        } else if (available instanceof Boolean) {
                            dto.setAvailable((Boolean) available);
                        } else {
                            dto.setAvailable(false);
                        }
                        
                        dto.setSku((String) product.get("sku"));
                        dto.setBrand((String) product.get("brand"));
                        
                        // Handle metafields for benefits and ingredients
                        Object metafieldsObj = product.get("metafields");
                        if (metafieldsObj instanceof Map) {
                            Map<String, Object> metafields = (Map<String, Object>) metafieldsObj;
                            
                            // Extract benefits
                            Object benefitsValue = metafields.get("benefits");
                            if (benefitsValue instanceof String) {
                                // Parse HTML content and extract text
                                String benefitsHtml = (String) benefitsValue;
                                // Remove HTML tags and split by list items or other delimiters
                                String benefitsText = benefitsHtml.replaceAll("<[^>]*>", " ").trim();
                                // Split by newlines or other delimiters and clean up
                                dto.setBenefits(Arrays.stream(benefitsText.split("\\s*\\n\\s*"))
                                    .filter(s -> !s.trim().isEmpty())
                                    .map(String::trim)
                                    .collect(Collectors.toList()));
                            }
                            
                            // Extract ingredients
                            Object ingredientsValue = metafields.get("ingredients");
                            if (ingredientsValue instanceof String) {
                                // Parse HTML content and extract text
                                String ingredientsHtml = (String) ingredientsValue;
                                // Remove HTML tags and clean up
                                String ingredientsText = ingredientsHtml.replaceAll("<[^>]*>", " ").trim();
                                // Split by commas or other appropriate delimiters
                                dto.setIngredients(Arrays.stream(ingredientsText.split("\\s*[,;]\\s*"))
                                    .filter(s -> !s.trim().isEmpty())
                                    .map(String::trim)
                                    .collect(Collectors.toList()));
                            }
                        }
                        
                        // Safely handle images
                        Object imagesObj = product.get("images");
                        if (imagesObj instanceof List) {
                            List<Map<String, Object>> imageMaps = (List<Map<String, Object>>) imagesObj;
                            List<amulProductsDTO.ProductImage> images = imageMaps.stream()
                                .filter(img -> img.get("image") != null)
                                .map(img -> new amulProductsDTO.ProductImage((String) img.get("image")))
                                .toList();
                            dto.setImages(images);
                        } else {
                            dto.setImages(List.of());
                        }
                        
                        return dto;
                    } catch (Exception e) {
                        log.error("Error mapping product: " + product, e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();
        } catch (Exception e) {
            log.error("Error in getAmulProductsDTO", e);
            throw new RuntimeException("Failed to process products: " + e.getMessage(), e);
        }
    }
    
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAmulProducts() {
        String tidHeader = authManager.generateTidHeader();
        String cookieHeader = cookieManager.getCookiesAsString();
        String substoreId = "66505ff06510ee3d5903fd42"; // Gujarat

        log.info("Fetching products with TID: {}", tidHeader);
        log.info("Using cookies: {}", cookieHeader);

        try {
            return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/api/1/entity/ms.products")
                    // All fields from amul-notify
                    .queryParam("fields[name]", "1")
                    .queryParam("fields[brand]", "1")
                    .queryParam("fields[categories]", "1")
                    .queryParam("fields[collections]", "1")
                    .queryParam("fields[alias]", "1")
                    .queryParam("fields[sku]", "1")
                    .queryParam("fields[price]", "1")
                    .queryParam("fields[compare_price]", "1")
                    .queryParam("fields[original_price]", "1")
                    .queryParam("fields[images]", "1")
                    .queryParam("fields[metafields]", "1")
                    .queryParam("fields[discounts]", "1")
                    .queryParam("fields[catalog_only]", "1")
                    .queryParam("fields[is_catalog]", "1")
                    .queryParam("fields[seller]", "1")
                    .queryParam("fields[available]", "1")
                    .queryParam("fields[inventory_quantity]", "1")
                    .queryParam("fields[net_quantity]", "1")
                    .queryParam("fields[num_reviews]", "1")
                    .queryParam("fields[avg_rating]", "1")
                    .queryParam("fields[inventory_low_stock_quantity]", "1")
                    .queryParam("fields[inventory_allow_out_of_stock]", "1")
                    .queryParam("fields[default_variant]", "1")
                    .queryParam("fields[variants]", "1")
                    .queryParam("fields[lp_seller_ids]", "1")
                    
                    // Category filter - using 'protein' as value (not ID)
                    .queryParam("filters[0][field]", "categories")
                    .queryParam("filters[0][value][0]", "protein")
                    .queryParam("filters[0][operator]", "in")
                    .queryParam("filters[0][original]", "1")
                    
                    // Other required params
                    .queryParam("facets", "true")
                    .queryParam("facetgroup", "default_category_facet")
                    .queryParam("limit", "24")
                    .queryParam("total", "1")
                    .queryParam("start", "0")
                    .queryParam("cdc", "1m")
                    .queryParam("substore", substoreId)
                    .build())
                .header("Cookie", cookieHeader)
                .header("tid", tidHeader)
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                .header("Accept", "application/json, text/plain, */*")
                .header("Cache-Control", "no-cache")
                .header("Pragma", "no-cache")
                .header("Frontend", "1")
                .header("Priority", "u=1, i")
                .header("Sec-Ch-Ua", "\"Google Chrome\";v=\"137\", \"Chromium\";v=\"137\", \"Not/A)Brand\";v=\"24\"")
                .header("Sec-Ch-Ua-Mobile", "?0")
                .header("Sec-Ch-Ua-Platform", "\"macOS\"")
                .header("Sec-Fetch-Dest", "empty")
                .header("Sec-Fetch-Mode", "cors")
                .header("Sec-Fetch-Site", "same-origin")
                .header("Sec-Gpc", "1")
                .header("base_url", "https://shop.amul.com/en/browse/protein")
                .header("Accept-Language", "en-US,en;q=0.9")
                .header("Origin", "https://shop.amul.com")
                .header("Referer", "https://shop.amul.com/")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .doOnSuccess(response -> {
                    log.info("Successfully fetched products");
                    if (response.get("data") == null || ((List<?>)response.get("data")).isEmpty()) {
                        log.warn("No products found in response");
                    } else {
                        log.info("Found {} products", ((List<?>)response.get("data")).size());
                    }
                })
                .doOnError(e -> log.error("Error fetching products: {}", e.getMessage(), e))
                .block();
        } catch (Exception e) {
            log.error("Exception in getAmulProducts: {}", e.getMessage(), e);
            throw e;
        }
    }
}