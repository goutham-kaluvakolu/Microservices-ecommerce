package com.ecommers.inventory_service.events;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

/**
 * Represents an event published when a new order is successfully initiated in the Order Service.
 * This event acts as the trigger for various downstream processes (e.g., inventory reservation, payment processing).
 * Shipping address is excluded for now.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreatedEvent {
    private String orderId;
    private String userId; // Corrected: Changed type from UUID to String
    private BigDecimal totalAmount;
    private String currency;
    private Instant timestamp;
    private List<OrderItemEvent> items;
    // ShippingAddressEvent shippingAddress; // Removed for now

    /**
     * Nested class for individual items within the order.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItemEvent {
        private String productId; // Corrected: Changed type from UUID to String
        private int quantity;
        private BigDecimal priceAtOrderTime;
    }

    // ShippingAddressEvent class is now completely removed as it's not used.
}
