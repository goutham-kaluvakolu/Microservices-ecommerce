package com.ecommers.inventory_service.controller;

import java.util.List;
import java.util.ArrayList;
        
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommers.inventory_service.dto.InventoryItemDto;
import com.ecommers.inventory_service.service.InventoryService;
import com.shop.api.ApiResponse;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventroyController {
    @Autowired
    private InventoryService inventoryService;
    @GetMapping("/ping")
    public String ping() {
        return "inventory service";
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<InventoryItemDto>>  getInventory(@PathVariable String productId) {
        // InventoryItemDto inventoryItemDto = inventoryService.getInventory(productId);
        InventoryItemDto inventoryItemDto = InventoryItemDto.builder()
            .productCode(productId)
            .totalStock(10)
            .availableStock(10)
            .reservedStock(0)
            .build();
        return ResponseEntity.ok(ApiResponse.success(inventoryItemDto, "1234567890"));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<InventoryItemDto>>> getInventory() {
        // List<InventoryItemDto> inventoryItemDtos = inventoryService.getInventory();
        List<InventoryItemDto> inventoryItemDtos = new ArrayList<>();
        inventoryItemDtos.add(InventoryItemDto.builder()
            .productCode("123")
            .totalStock(10)
            .availableStock(10)
            .reservedStock(0)
            .build());
        return ResponseEntity.ok(ApiResponse.success(inventoryItemDtos, "1234567890"));
    }

    //get the quantity of the product
    @GetMapping("/product/quantity/{productId}")
    public ResponseEntity<ApiResponse<Integer>> getQuantity(@PathVariable String productId) {
        // Integer quantity = inventoryService.getQuantity(productId);
        Integer quantity = 10;
        //return the quantity
        return ResponseEntity.ok(ApiResponse.success(quantity, "1234567890"));
    }

    //add product to inventory
    @PostMapping("/product/add")
    public ResponseEntity<ApiResponse<InventoryItemDto>> addProductToInventory(@RequestBody InventoryItemDto inventoryItemDto) {
        // InventoryItemDto inventoryItemDto2 = inventoryService.addProductToInventory(inventoryItemDto);
        return ResponseEntity.ok(ApiResponse.success(inventoryItemDto, "1234567890"));
    }
 
// GET /inventory/{productId}
// Response: { "productId": "...", "availableStock": ... }










}
