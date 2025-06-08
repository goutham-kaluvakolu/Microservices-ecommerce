package order_service.microservice.controller;

import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// import order_service.microservice.dto.OrderDto;
import com.shop.models.OrderDto;
import com.shop.models.OrderItemDto;
import com.shop.api.ApiResponse;
import order_service.microservice.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
public class OrdersController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Controller is working!");
    }
    //controller to test the kafka producer
    // @GetMapping("/test-kafka")
    // public ResponseEntity<String> testKafka() {
    //     orderService.testKafka();
    //     return ResponseEntity.ok("Kafka test message sent!");
    // }
    //controller to get all orders

    @GetMapping("/get/{orderId}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(@PathVariable String orderId) {
        if(orderId.length()%2==0){
            //sucess
            //dummy orderdto
            OrderDto orderDto = OrderDto.builder()
                .orderId(orderId)
                .userId("1234567890")
                .totalAmount(BigDecimal.valueOf(789))
                // .status(String.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
            ApiResponse<OrderDto> response = ApiResponse.success(orderDto, "1234567890");
            return ResponseEntity.ok(response);
        }
        else{
            //error
            ApiResponse<OrderDto> response = ApiResponse.error("ORDER_ERROR", "Failed to get order", "1234567890");
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders() {
        List<OrderDto> orders = orderService.getOrders();
        return ResponseEntity.ok(orders);
    }
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OrderDto>> placeOrder(@RequestBody String userId) {
        if(userId.length()%2==0){
            //sucess
            //dummy orderdto
            // OrderDto orderDto = OrderDto.builder()
            //     .orderId("1234567890")
            //     .userId(userId)
            //     .totalAmount(BigDecimal.valueOf(789))
            //     // .status(String.PENDING)
            //     .createdAt(LocalDateTime.now())
            //     .updatedAt(LocalDateTime.now())
            //     .items(List.of(OrderItemDto.builder()
            //         .productId("1234567890")
            //         .productName("Product 1")
            //         .build()))
            //     .build();
            OrderDto orderDto = orderService.placeOrder(userId);
            ApiResponse<OrderDto> response = ApiResponse.success(orderDto, "1234567890");
            return ResponseEntity.ok(response);
        }
        else{
            //error
            ApiResponse<OrderDto> response = ApiResponse.error("ORDER_ERROR", "Failed to place order", "1234567890");
            return ResponseEntity.badRequest().body(response);
        }
    }

    //get order based on order id
    
}
