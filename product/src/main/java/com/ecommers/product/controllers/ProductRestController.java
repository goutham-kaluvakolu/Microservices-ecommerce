package com.ecommers.product.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList; 
import org.springframework.http.ResponseEntity;

import com.ecommers.product.dto.ProductCreateRequestDTO;
import com.ecommers.product.dto.ProductDto;
import com.shop.api.ApiResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/v1/product")
public class ProductRestController {
    
    @GetMapping("/ping")
    public String getProduct() {
        return "Product";
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<ProductDto>> addProduct(@RequestBody ProductCreateRequestDTO productCreateRequestDTO) {
        //dummy product dto
        ProductDto productDto = ProductDto.builder()
            .name(productCreateRequestDTO.getName())
            .price(BigDecimal.valueOf(productCreateRequestDTO.getPrice()))
            .build();
        
        return ResponseEntity.ok(ApiResponse.success(productDto, "1234567890"));
    }

    @GetMapping("/get/name/{name}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductByName(@PathVariable String name) {
        //dummy product dto
        ProductDto productDto = ProductDto.builder()
            .name(name)
            .price(BigDecimal.valueOf(100))
            .build();
        return ResponseEntity.ok(ApiResponse.success(productDto, "1234567890"));
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable String id) {
        //dummy product dto
        ProductDto productDto = ProductDto.builder()
            // .id(id)
            .name("Product 1")
            .price(BigDecimal.valueOf(100))
            .build();
        //get product by id from database
        // Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
        return ResponseEntity.ok(ApiResponse.success(productDto, "1234567890"));
    }

    // /api/v1/products?category={category}
    @GetMapping("/get/category/{category}")
        public ResponseEntity<ApiResponse<List<ProductDto>>> getProductByCategory(@PathVariable String category) {
        //dummy product dto
        List<ProductDto> productDtoList = new ArrayList<>(  Arrays.asList(
            ProductDto.builder()
                .name("Product 1")
                .price(BigDecimal.valueOf(100))
                .build(),
            ProductDto.builder()
                .name("Product 2")
                .price(BigDecimal.valueOf(200))
                .build()
            ));
        
            return ResponseEntity.ok(ApiResponse.success(productDtoList, "1234567890"));
    }

//delete product by id
@DeleteMapping("/delete/{id}")
public ResponseEntity<ApiResponse<ProductDto>> deleteProductById(@PathVariable String id) {
    //delete product by id from database
    // Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"));
    return ResponseEntity.ok(ApiResponse.success(null, "1234567890"));
}


}