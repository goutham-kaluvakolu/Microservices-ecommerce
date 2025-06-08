package com.ecommers.cart_service.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.shop.api.ApiResponse;
import com.shop.models.CartItemDto;
import com.shop.models.CartResponseDto;
import com.ecommers.cart_service.dto.CartInputDto;
import com.ecommers.cart_service.dto.CartSummaryDto;


@RestController
@RequestMapping("/api/v1/cart")
public class CartServiceController {

    @GetMapping("/ping")
    public String getCart() {
        return "Cart";
    }

    //get cart for the user
    //GET /api/v1/cart/get/{userId}
    @GetMapping("/get/{userId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> getCart(@PathVariable String userId) {
        //dummy cart response dto
        CartResponseDto cartResponseDto = CartResponseDto.builder()
            .userId(userId)
            .items(List.of(CartItemDto.builder()
                .productId("prod-789")
                .quantity(2)
                .build()))
            .totalItems(2)
            .lastUpdated(LocalDateTime.now())
            .build();
        return ResponseEntity.ok(ApiResponse.success(cartResponseDto, "1234567890"));
    }

    @GetMapping("/get/internal/{userId}")
    public CartResponseDto getCartInternal(@PathVariable String userId) {
        //dummy cart response dto
        CartResponseDto cartResponseDto = CartResponseDto.builder()
            .userId(userId)
            .items(List.of(CartItemDto.builder()
                .productId("prod-789")
                .quantity(2)
                .build()))
            .totalItems(2)
            .lastUpdated(LocalDateTime.now())
            .build();
        return cartResponseDto;
    }

    //get cart summary for the user
    // GET /api/v1/cart/summary/{userId}
    @GetMapping("/summary/{userId}")
    public ResponseEntity<ApiResponse<CartSummaryDto>> getCartSummary(@PathVariable String userId) {
        //dummy cart summary dto
        CartSummaryDto cartSummaryDto = CartSummaryDto.builder()
            .totalItems(2)
            .uniqueProducts(2)
            .lastActivity(LocalDateTime.now())
            .build();
        return ResponseEntity.ok(ApiResponse.success(cartSummaryDto, "1234567890"));
    }

    //add item to cart for the user and return the updated cart response dto
    //POST /api/v1/cart/item
    @PostMapping("/item")
    public ResponseEntity<ApiResponse<CartResponseDto>> addToCart(@RequestBody CartInputDto cartInputDto) {
        //dummy cart item dto
        CartItemDto cartItemDto = CartItemDto.builder()
            .userId(cartInputDto.getUserId())
            .productId(cartInputDto.getProductId())
            .quantity(cartInputDto.getQuantity())
            .addedAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
        //dummy cart response dto
        CartResponseDto cartResponseDto = CartResponseDto.builder()
            .userId(cartInputDto.getUserId())
            .items(List.of(cartItemDto))
            .totalItems(2)
            .lastUpdated(LocalDateTime.now())
            .build(); 
        return ResponseEntity.ok(ApiResponse.success(cartResponseDto, "1234567890"));
    }

    // PUT /api/v1/cart/item/{productId}
    //update item in cart for the user and return the updated cart response dto
    @PutMapping("/item/{productId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> updateCartItem(@PathVariable String productId, @RequestBody CartInputDto cartInputDto) {
        //dummy cart item dto
        CartItemDto cartItemDto = CartItemDto.builder()
            .productId(productId)
            .quantity(cartInputDto.getQuantity())
            .build();
        //dummy cart response dto
        CartResponseDto cartResponseDto = CartResponseDto.builder()
            .userId(cartInputDto.getUserId())
            .items(List.of(cartItemDto))
            .totalItems(2)
            .lastUpdated(LocalDateTime.now())
            .build();
        return ResponseEntity.ok(ApiResponse.success(cartResponseDto, "1234567890"));
    }

    // DELETE /api/v1/cart/item/{productId}/{userId}
    //remove item from cart for the user and return the updated cart response dto
    @DeleteMapping("/item/{productId}/{userId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> deleteCartItem(@PathVariable String productId, @PathVariable String userId) {
        //dummy cart response dto
        //remove item from cart for the user and return the updated cart response dto
        CartResponseDto cartResponseDto = CartResponseDto.builder()
            .userId(userId)
            .items(List.of())
            .totalItems(0)
            .lastUpdated(LocalDateTime.now())
            .build();
        return ResponseEntity.ok(ApiResponse.success(cartResponseDto, "1234567890"));
    }

    //remove all items from cart for the user and return the updated cart response dto
    @DeleteMapping("/remove/all/{userId}")
    public ResponseEntity<ApiResponse<CartResponseDto>> removeAllItemsFromCart(@PathVariable String userId) {
        //dummy cart response dto
        CartResponseDto cartResponseDto = CartResponseDto.builder()
            .userId(userId)
            .items(List.of())
            .totalItems(0)
            .lastUpdated(LocalDateTime.now())
            .build();
        return ResponseEntity.ok(ApiResponse.success(cartResponseDto, "1234567890"));
    }


}
