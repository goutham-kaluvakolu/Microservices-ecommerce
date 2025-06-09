// src/main/java/com/example/orderservice/outbox/OutboxEvent.java
package com.ecommers.inventory_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * JPA Entity for the 'outbox' table, implementing the Transactional Outbox pattern.
 * Stores events that are to be published reliably.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "outbox")
public class OutboxEvent {

    // Unique identifier for the event, typically a UUID.
    @Id
    @Column(name = "id", length = 50, nullable = false)
    private String id;

    // Type of event (e.g., "OrderCreated", "ProductUpdated").
    @Column(name = "event_type", length = 50, nullable = false)
    private String eventType;

    // ID of the aggregate (entity) that published this event (e.g., the Order ID).
    @Column(name = "aggregate_id", length = 50, nullable = false)
    private String aggregateId;


   //storing as json object
   @JdbcTypeCode(SqlTypes.JSON)
   @Column(columnDefinition = "json")
   private JsonNode payload;

    // Current status of the event (PENDING, SENT, ACKNOWLEDGED, FAILED).
    // Stored as a VARCHAR, now mapped directly to String.
    @Column(name = "status", length = 50, nullable = false)
    private String status; // Changed from OutboxEventStatus enum to String

    // Optional: A correlation ID for tracing events across different services.
    @Column(name = "correlation_id", length = 50)
    private String correlationId;

    // JSON array of services/recipients expected to acknowledge processing.
    // Stored as TEXT in DB, handled as List<String> in Java via conversion.
    @Lob
    @Column(name = "expected_acknowledgments")
    private String expectedAcknowledgmentsJson; // Store as JSON string in DB

    // JSON array of services/recipients that have acknowledged processing.
    // Stored as TEXT in DB, handled as List<String> in Java via conversion.
    @Lob
    @Column(name = "received_acknowledgments")
    private String receivedAcknowledgmentsJson; // Store as JSON string in DB

    // Number of times this event has been retried for publishing.
    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0; // Default value set at entity level

    // Timestamp when the event was created in the outbox.
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    // Optional: Timestamp when an acknowledgment for this event is expected.
    @Column(name = "ack_timeout_at")
    private Instant ackTimeoutAt;


    // Override toString for better logging/debugging
    @Override
    public String toString() {
        return "OutboxEvent{" +
               "id='" + id + '\'' +
               ", eventType='" + eventType + '\'' +
               ", aggregateId='" + aggregateId + '\'' +
               ", status='" + status + '\'' + // Updated for String
               ", retryCount=" + retryCount +
               ", createdAt=" + createdAt +
               '}';
    }
}
