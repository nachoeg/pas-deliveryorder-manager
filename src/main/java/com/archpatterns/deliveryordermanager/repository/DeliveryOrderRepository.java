package com.archpatterns.deliveryordermanager.repository;

import com.archpatterns.deliveryordermanager.enums.DeliveryStatus;
import com.archpatterns.deliveryordermanager.model.DeliveryOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryOrderRepository extends JpaRepository<DeliveryOrder, Long> {

    // Buscar órdenes por ID de comprador
    List<DeliveryOrder> findAllByBuyer_Id(Long buyerId);

    // Buscar órdenes por ID de repartidor
    List<DeliveryOrder> findAllByDeliver_Id(Long deliveryId);

    // Buscar todas las órdenes con un estado específico (PENDING, DELIVERED, etc.)
    List<DeliveryOrder> findAllByStatus(DeliveryStatus status);
}
