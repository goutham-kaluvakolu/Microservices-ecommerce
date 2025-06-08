package com.ecommers.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartInputDto {
    // {
    //     "userId": "user-123",
    //     "productId": "prod-789",
    //     "quantity": 2
    //   }

    private String userId;
    private String productId;
    private int quantity;
}