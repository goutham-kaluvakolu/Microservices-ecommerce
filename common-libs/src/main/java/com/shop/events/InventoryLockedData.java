package com.shop.events;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor 
@AllArgsConstructor
@Builder
public class InventoryLockedData {
    private String orderId;
    private String reservationId;
    private List<Map<String, Object>> lockedItems; // Or specific Item class
    private String status;
}