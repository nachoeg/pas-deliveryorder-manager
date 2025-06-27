package com.archpatterns.deliveryordermanager.service;

import com.archpatterns.deliveryordermanager.dto.DeliveryOrderRequest;
import com.archpatterns.deliveryordermanager.dto.DeliveryOrderResponse;
import com.archpatterns.deliveryordermanager.enums.DeliveryStatus;

import java.util.List;

public interface DeliveryOrderService {

    // Crear nueva orden
    DeliveryOrderResponse createOrder(DeliveryOrderRequest request);

    // Buscar órdenes por ID del comprador
    List<DeliveryOrderResponse> getOrdersByBuyer(Long buyerId);

    // Buscar órdenes por ID del repartidor
    List<DeliveryOrderResponse> getOrdersByDeliver(Long deliveryId);

    // Buscar por estado (PENDING, DELIVERED, etc.)
    List<DeliveryOrderResponse> getOrdersByStatus(DeliveryStatus status);

    // Marcar como tomada por un repartidor
    DeliveryOrderResponse pickOrder(Long deliveryOrderId, Long deliverId);

    // Marcar como entregada
    DeliveryOrderResponse deliverOrder(Long deliveryOrderId);

    // Marcar como cancelada
    DeliveryOrderResponse cancelOrder(Long deliveryOrderId);

    // Calificar vendedor, producto y repartidor
    DeliveryOrderResponse qualifyOrder(Long deliveryOrderId, int productStars, int sellerStars, int deliveryStars);
}
