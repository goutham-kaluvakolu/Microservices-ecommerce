package com.ecommers.product.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequestDTO {
    // {
    //     "name": "iPhone 15 Pro",
    //     "description": "Latest iPhone with advanced features",
    //     "sku": "IPHONE15PRO-256GB",
    //     "category": "Electronics",
    //     "brand": "Apple",
    //     "price": 1199.99,
    //     "currency": "USD",
    //     "weight": 0.221,
    //     "dimensions": {
    //       "length": 15.95,
    //       "width": 7.67,
    //       "height": 0.83
    //     },
    //     "images": ["image1.jpg", "image2.jpg"],
    //     "tags": ["smartphone", "ios", "premium"],
    //     "status": "ACTIVE"
    //   }
    private String name;
    private String description;
    private String sku;
    private String category;
    private String brand;
    private double price;
    private String currency;
    private double weight;
    private Map<String, Double> dimensions;
    private List<String> images;
    private List<String> tags;
    private String status;
      
}
