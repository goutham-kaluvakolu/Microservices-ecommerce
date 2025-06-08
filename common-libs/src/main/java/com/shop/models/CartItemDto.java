package com.shop.models;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    // {
    //     "userId": "string",
    //     "productId": "string", 
    //     "quantity": "integer",
    //     "addedAt": "timestamp",
    //     "updatedAt": "timestamp"
    //   }
    private String userId;
    private String productId;
    private int quantity;
    private LocalDateTime addedAt;
    private LocalDateTime updatedAt;
}