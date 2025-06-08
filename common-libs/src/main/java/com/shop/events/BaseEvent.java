package com.shop.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

// {
//   "eventId": "string (UUID)",         // Unique identifier for this specific event instance
//   "eventType": "string",              // A unique, uppercase name identifying the type of event (e.g., "ORDER_CREATED")
//   "eventVersion": "string (v1.0)",    // Version of the event schema, allowing for future changes
//   "timestamp": "ISO 8601 timestamp",  // When the event occurred (e.g., "2025-06-03T21:13:24Z")
//   "correlationId": "string",          // Tracing ID, carried across service calls/events for a single business transaction
//   "source": "string (service-name)",  // The name of the service that published this event (e.g., "order-service")
//   "data": { /* event-specific data payload */ }, // The actual business data related to the event
//   "metadata": {
//     "traceId": "string",              // More granular tracing ID, useful for distributed tracing tools
//     "userId": "string (optional)"     // User ID who initiated the action, if applicable
//   }
// }

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEvent {
    private String eventId;
    private String eventType;
    private String eventVersion;
    private String timestamp;
    private String correlationId;
    private String source;
    private Object data;
    private Map<String, String> metadata;
}

