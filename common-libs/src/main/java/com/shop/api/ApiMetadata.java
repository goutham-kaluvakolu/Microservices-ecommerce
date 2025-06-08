// common-libs/src/main/java/com/shop/api/ApiMetadata.java
package com.shop.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant; // For precise timestamp in UTC

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiMetadata {
    private Instant timestamp;    // Use Instant for UTC timestamp (e.g., "2025-06-03T21:13:24Z")
    private String correlationId; // For tracing requests across microservices
    private String version;       // API version, useful for deprecation handling etc.
}