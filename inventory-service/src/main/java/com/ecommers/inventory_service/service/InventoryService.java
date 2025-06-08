package com.ecommers.inventory_service.service;

// import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommers.inventory_service.dto.InventoryItemDto;
// import com.ecommers.inventory_service.dto.InventoryReservedItemDto;
import com.ecommers.inventory_service.entity.InventoryItem;
// import com.ecommers.inventory_service.entity.InventoryReservation;
// import com.ecommers.inventory_service.entity.ReservationStatus;
// import com.ecommers.inventory_service.events.InventoryReservedEventFail;
// import com.ecommers.inventory_service.events.InventoryReservedEventSuccess;
import com.ecommers.inventory_service.events.OrderCreatedEvent;
// import com.ecommers.inventory_service.producer.InventoryEventProducer;
import com.ecommers.inventory_service.repository.InventoryRepository;
import com.ecommers.inventory_service.repository.InventoryReservationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InventoryService {

    private  InventoryReservationRepository inventoryReservationRepository;
    
    private InventoryRepository inventoryRepository;

    // private InventoryEventProducer inventoryEventProducer;

    // public InventoryService(InventoryReservationRepository inventoryReservationRepository, InventoryRepository inventoryRepository, InventoryEventProducer inventoryEventProducer) {
    //     this.inventoryReservationRepository = inventoryReservationRepository;
    //     this.inventoryRepository = inventoryRepository;
    //     // this.inventoryEventProducer = inventoryEventProducer;
    // }
    public InventoryService(InventoryReservationRepository inventoryReservationRepository, InventoryRepository inventoryRepository) {
        this.inventoryReservationRepository = inventoryReservationRepository;
        this.inventoryRepository = inventoryRepository;
        // this.inventoryEventProducer = inventoryEventProducer;
    }

    public InventoryItemDto getInventory(String productCode) {
        InventoryItem item = inventoryRepository.findByProductCode(productCode);
        return new InventoryItemDto(
            item.getProductCode(),
            item.getTotalStock(),
            item.getAvailableStock(),
            item.getReservedStock(),
            item.getLastUpdated()
        );
    }

    public List<InventoryItemDto> getInventory() {
        List<InventoryItem> items = inventoryRepository.findAll();
        return items.stream()
            .map(item -> new InventoryItemDto(
                item.getProductCode(),
                item.getTotalStock(),
                item.getAvailableStock(),
                item.getReservedStock(),
                item.getLastUpdated()
            ))
            .collect(Collectors.toList());
    }

    @Transactional // Ensures atomicity for all DB operations within this method
    public void handleOrderCreatedEvent(OrderCreatedEvent event) {
        String testStatus = "fail";
        log.info("Processing order event for orderId: {}", event.getOrderId());

        // Iterate over each item in the order to reserve inventory
        event.getItems().forEach(item -> {
            String productCode = item.getProductId(); // Assuming OrderItemEvent has getProductId()
            int requestedQuantity = item.getQuantity();

            // 1. Fetch the InventoryItem. JPA automatically reads the @Version.
            // Use .orElseThrow() for cleaner error handling if product not found.
            InventoryItem inventoryItem = inventoryRepository.findByProductCode(productCode);
            if (inventoryItem == null) {
                log.error("Product not found in inventory: {}", productCode);
                throw new RuntimeException("Product not found in inventory: " + productCode);
            }

            // 2. Check for sufficient available stock
            if (inventoryItem.getAvailableStock() < requestedQuantity) {
                log.error("Insufficient available stock for product: {}. Available: {}, Requested: {}",
                        productCode, inventoryItem.getAvailableStock(), requestedQuantity);
                throw new RuntimeException("Insufficient available stock for product: " + productCode);
            }

            

            // 5. Save the updated InventoryItem.
            // JPA will automatically handle the version increment and optimistic locking check here.
            // If a concurrent update occurred, an OptimisticLockingFailureException will be thrown.
            try {
                if(testStatus.equals("fail")){
                    throw new OptimisticLockingFailureException("Test failed");
                }
                // 3. Update stock quantities in the InventoryItem entity
            inventoryItem.setAvailableStock(inventoryItem.getAvailableStock() - requestedQuantity);
            inventoryItem.setReservedStock(inventoryItem.getReservedStock() + requestedQuantity);

                inventoryRepository.save(inventoryItem);
                // InventoryReservedEventSuccess inventoryReservedEvent = InventoryReservedEventSuccess.builder()
                // .orderId(String.valueOf(event.getOrderId()))
                // .reservedItems(event.getItems().stream()
                // .map(orderItem -> InventoryReservedItemDto.builder()
                // .productId(orderItem.getProductId())
                // .reservedQuantity(orderItem.getQuantity())
                // .build())
                // .collect(Collectors.toList()))
                // .reservationStatus(ReservationStatus.CONFIRMED.name())
                // .build();
                // inventoryEventProducer.sendInventoryReservedEventSuccess(inventoryReservedEvent);
                // log.info("InventoryReservedEvent sent successfully to topic: {}", inventoryEventsTopicName);
            } catch (OptimisticLockingFailureException e) {

                // InventoryReservedEventFail inventoryReservedEvent = InventoryReservedEventFail.builder()
                // .orderId(String.valueOf(event.getOrderId()))
                // .reservedItems(event.getItems().stream()
                // .map(orderItem -> InventoryReservedItemDto.builder()
                // .productId(orderItem.getProductId())
                // .reservedQuantity(orderItem.getQuantity())
                // .build())
                // .collect(Collectors.toList()))
                // .reservationStatus(ReservationStatus.CANCELLED.name())
                // .build();
                // inventoryEventProducer.sendInventoryReservationFailedEvent(inventoryReservedEvent);

                log.warn("Optimistic locking conflict detected for product {}. Retrying operation.", productCode, e);
                // IMPORTANT: When this exception occurs, the current transaction will be rolled back.
                // The Kafka listener container (DefaultErrorHandler) will typically retry the message.
                // If you have custom retry logic, you might re-throw a specific exception to trigger it.
                // For now, re-throwing the OptimisticLockingFailureException will cause the Kafka listener
                // to retry the message based on its configured error handler.
                // throw e; // Re-throw to signal failure and trigger Kafka retry mechanism
            }


            log.info("Updated inventory for product: {}. New available: {}, New reserved: {}",
                    productCode, inventoryItem.getAvailableStock(), inventoryItem.getReservedStock());
        });

        // If all items are processed successfully within the loop, the transaction commits here.
        // If any exception (like RuntimeException or OptimisticLockingFailureException) is thrown,
        // the @Transactional annotation will cause the entire transaction to roll back,
        // reverting all changes (inventory updates and reservation insertions) for this order.
    }

    @Transactional
    public void addStock(String productCode, int quantity) {
        InventoryItem item = inventoryRepository.findByProductCode(productCode);
        if (item == null) {
            item = InventoryItem.builder()
                .productCode(productCode)
                .totalStock(quantity)
                .availableStock(quantity)
                .reservedStock(0)
                .build();
        } else {
            item.setTotalStock(item.getTotalStock() + quantity);
            item.setAvailableStock(item.getAvailableStock() + quantity);
        }
        inventoryRepository.save(item);
        log.info("Added {} units to product: {}. New total: {}, New available: {}", 
            quantity, productCode, item.getTotalStock(), item.getAvailableStock());
    }

    @Transactional
    public void updateStock(String productCode, int totalQuantity) {
        InventoryItem item = inventoryRepository.findByProductCode(productCode);
        if (item == null) {
            throw new RuntimeException("Product not found in inventory: " + productCode);
        }
        
        int currentReserved = item.getReservedStock();
        if (totalQuantity < currentReserved) {
            throw new RuntimeException("Cannot set total stock below reserved quantity: " + currentReserved);
        }
        
        item.setTotalStock(totalQuantity);
        item.setAvailableStock(totalQuantity - currentReserved);
        inventoryRepository.save(item);
        log.info("Updated stock for product: {} to total: {}, available: {}, reserved: {}", 
            productCode, item.getTotalStock(), item.getAvailableStock(), item.getReservedStock());
    }
}
