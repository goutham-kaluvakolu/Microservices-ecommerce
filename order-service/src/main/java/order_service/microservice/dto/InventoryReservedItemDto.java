package order_service.microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReservedItemDto {
    private String productId;
    private Integer reservedQuantity; // Quantity that was successfully reserved
    private String orderId;
}
