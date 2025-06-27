package com.archpatterns.deliveryordermanager.repository;

import com.archpatterns.deliveryordermanager.model.CardItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CardItemRepository extends JpaRepository<CardItem, Long> {

    // Ejemplo opcional: buscar todos los ítems de una orden específica
    //List<CardItem> findAllByDeliveryOrder_Id(Long deliveryOrderId);

    // Ejemplo opcional: buscar ítems por producto
    List<CardItem> findAllByProductId(Long productId);
}
