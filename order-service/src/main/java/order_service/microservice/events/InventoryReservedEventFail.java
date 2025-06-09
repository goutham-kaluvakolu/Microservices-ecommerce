package order_service.microservice.events;

// import com.ecommers.inventory_service.entity.InventoryReservation.InventoryReservationBuilder;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.shop.events.InventoryReservedItemDto;

// This DTO will likely be re-used from your order-service,
// or defined identically in inventory-service if it's a shared contract
// For simplicity, let's assume InventoryReservedItemDto is defined similarly to OrderItemDto
// but represents the items that were successfully reserved.
// If you have a shared 'common-events' module, this would live there.
// For now, let's assume a dedicated DTO for the event's items.

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReservedEventFail {
    private String eventId; // Unique ID for this specific event (UUID.randomUUID().toString())
    private String orderId; // The ID of the order this reservation relates to (e.g., your orderId)
    private String userId;  // The ID of the user who placed the order
    private LocalDateTime timestamp; // When this event was generated

    // A list of items that were successfully reserved
    private List<InventoryReservedItemDto> reservedItems;

    private String reservationStatus; // Could be "SUCCESS" or "PARTIAL_SUCCESS" if some items failed
    private String message; // Optional: A descriptive message, e.g., "All items reserved successfully."

}
