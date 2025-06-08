package com.ecommers.cart_service.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartSummaryDto {
    // {
    //     "totalItems": 5,
    //     "uniqueProducts": 3,
    //     "lastActivity": "2025-06-02T10:35:00Z"
    //   }

      private int totalItems;
      private int uniqueProducts;
      private LocalDateTime lastActivity;
}
