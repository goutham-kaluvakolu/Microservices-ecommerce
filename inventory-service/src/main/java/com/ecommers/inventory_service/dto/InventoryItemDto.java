package com.ecommers.inventory_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItemDto {
    private String productCode;
    private Integer totalStock;
    private Integer availableStock;
    private Integer reservedStock;
    private LocalDateTime lastUpdated;
}
