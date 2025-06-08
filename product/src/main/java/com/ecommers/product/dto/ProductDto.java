package com.ecommers.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    public String name;
    public BigDecimal price;
}
