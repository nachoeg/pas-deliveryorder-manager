package com.archpatterns.deliveryordermanager.service;

import com.archpatterns.deliveryordermanager.dto.DeliveryOrderRequest;
import com.archpatterns.deliveryordermanager.dto.DeliveryOrderResponse;
import com.archpatterns.deliveryordermanager.dto.ChoreoData;
import com.archpatterns.deliveryordermanager.enums.DeliveryStatus;
import com.archpatterns.deliveryordermanager.exceptions.DeliveryOrderException;

import java.util.List;


public interface DeliveryOrderService {

    // Crear nueva orden
    DeliveryOrderResponse createOrder(DeliveryOrderRequest request) throws DeliveryOrderException;

    //crear nueva orden desde ChoreoData
    void createOrderFromChoreoData(ChoreoData request) throws DeliveryOrderException;

    // Buscar órdenes por ID del comprador
    List<DeliveryOrderResponse> getOrdersByBuyer(Long buyerId) throws DeliveryOrderException;

    // Buscar órdenes por ID del repartidor
    List<DeliveryOrderResponse> getOrdersByDeliver(Long deliveryId) throws DeliveryOrderException;

    // Buscar por estado (PENDING, DELIVERED, etc.)
    List<DeliveryOrderResponse> getOrdersByStatus(DeliveryStatus status) throws DeliveryOrderException;

    // Marcar como tomada por un repartidor
    DeliveryOrderResponse pickOrder(Long deliveryOrderId, Long deliverId) throws DeliveryOrderException;

    // Marcar como entregada
    DeliveryOrderResponse deliverOrder(Long deliveryOrderId, Long userId, String[] roles) throws DeliveryOrderException;

    // Marcar como cancelada
    DeliveryOrderResponse cancelOrder(Long deliveryOrderId, Long userId, String[] roles) throws DeliveryOrderException;

    // Calificar vendedor, producto y repartidor
    DeliveryOrderResponse qualifyOrder(Long deliveryOrderId, int productStars, int sellerStars, int deliveryStars, Long userId, String[] roles) throws DeliveryOrderException;

    Long extractUserIdFromCurrentRequest() throws DeliveryOrderException;
    String[] extractUserRolesFromCurrentRequest() throws DeliveryOrderException;
}