package com.ecommers.inventory_service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommers.inventory_service.entity.OutboxEvent;

public interface OutboxRepository extends JpaRepository<OutboxEvent, String> {

    List<OutboxEvent> findByStatus(String string);
    
}
