// common-libs/src/main/java/com/shop/api/ApiError.java
package com.shop.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiError {
    private String code;         // A unique error code (e.g., "RESOURCE_NOT_FOUND", "VALIDATION_ERROR")
    private String message;      // A human-readable error message
    private Map<String, Object> details; // Optional: A map for additional error details (e.g., field validation errors)
}