package com.ecommers.inventory_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommers.inventory_service.entity.InventoryItem;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    InventoryItem findByProductCode(String productCode);
}