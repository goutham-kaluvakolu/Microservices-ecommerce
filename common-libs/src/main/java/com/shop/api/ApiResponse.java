// common-libs/src/main/java/com/shop/api/ApiResponse.java
package com.shop.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> { // <T> makes this a generic class for the 'data' payload
    private boolean success;
    private T data;             // For successful responses, holds your specific DTO (e.g., OrderDTO, List<ProductDTO>)
    private ApiError error;     // For error responses
    private ApiMetadata metadata;


    public static <T> ApiResponse<T> success(T data, String correlationId) {
        ApiMetadata metadata = ApiMetadata.builder()
                .timestamp(Instant.now())
                .correlationId(correlationId)
                .version("1.0")
                .build();

        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .metadata(metadata)
                .build();
    }

    // For error responses
    public static <T> ApiResponse<T> error(String errorCode, String errorMessage, Map<String, Object> details, String correlationId) {
        ApiError error = ApiError.builder()
                .code(errorCode)
                .message(errorMessage)
                .details(details)
                .build();

        ApiMetadata metadata = ApiMetadata.builder()
                .timestamp(Instant.now())
                .correlationId(correlationId)
                .build();

        return ApiResponse.<T>builder()
                .success(false)
                .error(error)
                .metadata(metadata)
                .build();
    }

    // Optional: Overloads for simpler use
    public static <T> ApiResponse<T> error(String errorCode, String errorMessage, String correlationId) {
        return error(errorCode, errorMessage, null, correlationId); // No details map
    }
}