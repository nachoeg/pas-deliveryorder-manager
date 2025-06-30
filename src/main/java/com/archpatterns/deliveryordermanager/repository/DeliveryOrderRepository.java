package com.archpatterns.deliveryordermanager.repository;

import com.archpatterns.deliveryordermanager.enums.DeliveryStatus;
import com.archpatterns.deliveryordermanager.model.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {

    List<DeliveryOrder> findAllByBuyerId(Long buyerId);

    List<DeliveryOrder> findAllByDeliverId(Long deliverId);

    // Buscar todas las órdenes con un estado específico (PENDING, DELIVERED, etc.)
    List<DeliveryOrder> findAllByStatus(DeliveryStatus status);
}
