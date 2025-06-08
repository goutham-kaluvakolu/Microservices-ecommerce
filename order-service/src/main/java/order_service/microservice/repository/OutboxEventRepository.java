package order_service.microservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import order_service.microservice.entity.OutboxEvent;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, String> {

    // List<OutboxEvent> findTopNByStatusOrderByCreatedAtAsc(String statusPending, int limit);
    @Query("SELECT o FROM OutboxEvent o WHERE o.status = :status ORDER BY o.createdAt ASC LIMIT :limit")
    List<OutboxEvent> findEventsByStatusWithLimit(@Param("status") String status, @Param("limit") int limit);
}
