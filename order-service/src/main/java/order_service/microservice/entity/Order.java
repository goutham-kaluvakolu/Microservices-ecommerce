package order_service.microservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "total_price")
    private BigDecimal totalAmount;

    @Column(name = "status")
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    // 'mappedBy' indicates that the 'order' field in OrderItem owns the relationship.
    // 'cascade = CascadeType.ALL' ensures that persist, merge, remove operations on Order
    // will cascade to its OrderItems.
    // 'orphanRemoval = true' (optional but recommended for parent-child) means if an
    // OrderItem is removed from the 'items' list, it will be deleted from the DB.
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    // Helper method to ensure bidirectional consistency
    public void addOrderItem(OrderItem item) {
        items.add(item);
        item.setOrder(this); // Crucial: sets the foreign key on the child side
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
