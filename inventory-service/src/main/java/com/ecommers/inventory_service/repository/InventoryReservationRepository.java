package com.ecommers.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommers.inventory_service.entity.InventoryReservation;

public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, Long> {
    
}
