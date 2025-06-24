package com.archpatterns.deliveryordermanager.utils;

import com.archpatterns.deliveryordermanager.dto.DeliveryOrderResponse;
import com.archpatterns.deliveryordermanager.dto.ErrorDto;
import com.archpatterns.deliveryordermanager.exceptions.DeliveryOrderException;
import com.archpatterns.deliveryordermanager.model.DeliveryOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class Util {

    public static DeliveryOrderException genericError(Exception ex) {
        log.error("Se produjo el siguiente error: {}", ex.getMessage(), ex);
        return new DeliveryOrderException(ErrorDto.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Se produjo un error inesperado en la ejecución del proceso")
                .detail(ex.getMessage())
                .localizedException(ex.getClass().getName() + ": " + (ex.getCause() != null ? ex.getCause() : "Sin causa"))
                .build());
    }

    public static DeliveryOrderException notFound(String mensaje, Long id) {
        return new DeliveryOrderException(ErrorDto.builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(mensaje)
                .detail("No se encontró entidad con ID: " + id)
                .localizedException("DeliveryOrderServiceImpl")
                .build());
    }

    public static DeliveryOrderResponse convertToResponse(DeliveryOrder order) {
        return DeliveryOrderResponse.builder()
                .id(order.getId())
                .createTimestamp(order.getCreateTimestamp())
                .pickedTimeStamp(order.getPickedTimeStamp())
                .sellerStarts(order.getSellerStarts())
                .productStarts(order.getProductStarts())
                .deliveryStarts(order.getDeliveryStarts())
                .status(order.getStatus())
                .buyerId(order.getBuyer().getId())
                .deliveryId(order.getDeliver() != null ? order.getDeliver().getId() : null)
                .productId(order.getCardItem().getProductId())
                .quantity(order.getCardItem().getQuantity())
                .build();
    }
}
