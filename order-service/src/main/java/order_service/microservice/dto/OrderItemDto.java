// package order_service.microservice.dto.OrderItemDto
package order_service.microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    private String productId; // Changed to String
    private String productName;
    private BigDecimal productPrice;
    private Integer quantity;
}
