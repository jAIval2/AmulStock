package com.example.AmulStock.AmulStock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class amulProductsDTO{
    private String id;
    private String name;
    private String alias;
    private int price;
    private int comparePrice;
    private int inventoryQuantity;
    private int lowStockQuantity;
    private boolean available;
    private String sku;
    private String brand;
    private List<String> benefits;
    private List<String> ingredients;
    private List<ProductImage> images;
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductImage {
        private String image;
        private String fileBaseUrl = "https://shop.amul.com/s/62fa94df8c13af2e242eba16/";

        // Constructor that accepts just the image path
        public ProductImage(String image) {
            this.image = image;
        }

        public String getFullImageUrl() {
            return fileBaseUrl + image;
        }
    }
}
