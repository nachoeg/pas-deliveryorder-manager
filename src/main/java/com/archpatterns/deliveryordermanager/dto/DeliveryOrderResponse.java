package com.archpatterns.deliveryordermanager.dto;

import com.archpatterns.deliveryordermanager.enums.DeliveryStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryOrderResponse {

    // ID generado por la base de datos para la orden
    private Long id;

    // Fecha y hora en que se creó la orden (generado automáticamente)
    private LocalDateTime createTimestamp;

    // Fecha y hora en que el repartidor tomó la orden (se setea al hacer "pick")
    private LocalDateTime pickedTimeStamp;

    // Puntuación del vendedor (de 1 a 5 estrellas)
    private Integer sellerStarts;

    // Puntuación del producto (de 1 a 5 estrellas)
    private Integer productStarts;

    // Puntuación del repartidor (de 1 a 5 estrellas)
    private Integer deliveryStarts;

    // Estado actual de la orden (PENDING_ORDER, PICKEDUP_ORDER, etc.)
    private DeliveryStatus status;

    // ID del comprador que realizó la orden
    private Long buyerId;

    // ID del repartidor asignado (puede ser null si aún no fue tomado)
    private Long deliveryId;

    // ID del producto incluido en la orden
    private Long productId;

    // Cantidad solicitada del producto
    private Integer quantity;

    private Double priceTotal; 

    private String productName; 
}