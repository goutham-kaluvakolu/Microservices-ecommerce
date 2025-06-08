// package com.ecommers.product.service;

// import com.ecommers.product.Entity.Product;
// import com.ecommers.product.Repository.ProductRepository;
// import com.ecommers.product.dto.ProductDto;
// import com.ecommers.product.exceptions.ProductExisitException;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.stereotype.Service;
// import java.util.List;

// @Service
// @RequiredArgsConstructor
// @Slf4j

// public class ProductService {
//     public final ProductRepository productRepository;

//     public List<ProductDto> getProducts(){
//         log.info("Attempting to fetch all products from MongoDB");
//         List<Product> products = productRepository.findAll();
//         log.info("Found {} products in database", products.size());
//         log.info("Products from database: {}", products);
        
//         List<ProductDto> result = products.stream().map(product->{
//             return new ProductDto(product.getName(),product.getPrice());
//         }).toList();
//         log.info("Converted to DTOs: {}", result);
//         return result;
//     }
//     public void createProduct(ProductDto productDto) {
//         if(!productExists(productDto.getName())){
//             productRepository.save(constructProduct(productDto));
//         }
//         else{
//             throw new ProductExisitException("there are a product with that name in db");
//         }
//     }

//     private Product constructProduct(ProductDto productDto) {
//         String productName = productDto.getName().toLowerCase();
//         return Product.builder().name(productName).price(productDto.getPrice()).build();
//     }

//     private Boolean productExists(String name){
//         List<Product> x = productRepository.findAllByName(name);

//         return !x.isEmpty();
//     }

//     public List<ProductDto> getProductsSortedByPrice() {
//         List<Product> products = productRepository.findAllByPriceIsNotNullOrderByPriceDesc();
//         log.info(products.toString());
//         List<ProductDto> result = products.stream().map(product->{
//             return new ProductDto(product.getName(),product.getPrice());
//         }).toList();
//         return result;
//     }
// }
