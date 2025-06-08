package order_service.microservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import order_service.microservice.entity.OrderItem;

public interface OrderItemRepository extends
 JpaRepository<OrderItem, Long> {
}