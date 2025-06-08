package com.ecommers.inventory_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory") // Still maps to the 'inventory' table in your DB, but it now holds product-level stock
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Renamed from 'product_id' to 'product_code' in SQL, reflects the SKU
    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode; // Unique identifier for the product (SKU)

    // Renamed from 'stock_quantity' to 'total_stock' in SQL, represents total physical units
    @Column(name = "total_stock", nullable = false)
    private Integer totalStock;

    // New column: Stock immediately available for new reservations
    @Column(name = "available_stock", nullable = false)
    private Integer availableStock;

    // New column: Stock currently reserved for orders (soft or hard)
    @Column(name = "reserved_stock", nullable = false)
    private Integer reservedStock;

    // New column: Version for optimistic locking
    @Version // This annotation tells JPA to manage this column for optimistic locking
    @Column(name = "version", nullable = false)
    private Integer version;

    // Your existing last_updated column. Keep if you want JPA to manage it,
    // otherwise, the DB's ON UPDATE CURRENT_TIMESTAMP handles it.
    // If DB handles it, you can remove the @PrePersist/@PreUpdate methods.
    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // If your database handles 'ON UPDATE CURRENT_TIMESTAMP', you can remove these.
    // If not, keep them to ensure 'lastUpdated' is always current.
    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.lastUpdated = LocalDateTime.now();
    }
}