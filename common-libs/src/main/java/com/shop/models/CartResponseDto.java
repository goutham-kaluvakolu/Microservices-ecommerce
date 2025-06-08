package com.shop.models;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDto {
    // {
    //     "userId": "user-123",
    //     "items": [
    //       {
    //         "productId": "prod-789",
    //         "quantity": 2,
    //         "addedAt": "2025-06-02T10:30:00Z",
    //         "updatedAt": "2025-06-02T10:35:00Z"
    //       }
    //     ],
    //     "totalItems": 3,
    //     "lastUpdated": "2025-06-02T10:35:00Z"
    //   }

    private String userId;
    private List<CartItemDto> items;
    private int totalItems;
    private LocalDateTime lastUpdated;
      
}
