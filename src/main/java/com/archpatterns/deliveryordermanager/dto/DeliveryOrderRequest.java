package com.archpatterns.deliveryordermanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.archpatterns.deliveryordermanager.enums.DeliveryStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrderRequest {

    // ID del producto comprado (si viene de otra tabla y es Long)
    private Long productId;

    // Cantidad de unidades compradas del producto
    private Integer quantity;

    // ID del comprador que realiza la orden (usuario logueado o asociado)
    private Long buyerId;

    // ID del repartidor asignado (puede estar vacío al momento de crear la orden)
    private Long deliveryId;

    // Estado actual de la orden (por defecto debería iniciarse como PENDING_ORDER)
    private DeliveryStatus status;

    private Long cardItemId; 

    private Double priceTotal;
    
    private String productName;
}
