package order_service.microservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import order_service.microservice.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long>{

    void deleteByorderId(String orderId);
    
}
