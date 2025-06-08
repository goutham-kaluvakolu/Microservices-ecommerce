package com.ecommers.inventory_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_reservations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private String orderId; // Matches the order ID from your Order Service

    // Many-to-one relationship with InventoryItem (your 'products' table)
    @ManyToOne(fetch = FetchType.LAZY) // LAZY loading is generally good practice for performance
    @JoinColumn(name = "product_id", nullable = false) // This is the foreign key column in inventory_reservations
    private InventoryItem product; // The actual InventoryItem (product) being reserved

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Enumerated(EnumType.STRING) // Store enum names as strings in the DB
    @Column(name = "reservation_status", nullable = false)
    private ReservationStatus reservationStatus;

    @Column(name = "created_at", nullable = false, updatable = false) // updatable = false for created_at
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}