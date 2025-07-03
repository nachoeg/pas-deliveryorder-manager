package com.archpatterns.deliveryordermanager.client;

import com.archpatterns.deliveryordermanager.dto.Listdata;
import com.archpatterns.deliveryordermanager.dto.DataQueue;
import com.archpatterns.deliveryordermanager.exceptions.DeliveryOrderException;
import com.archpatterns.deliveryordermanager.service.DeliveryOrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Consumer {

    private final DeliveryOrderService deliveryOrderService;

    @RabbitListener(queues = { "${app.rabbitmq.make-order-queue}" })
    public void receiveMakeOrder(@Payload String messageJson) {
        log.info("Mensaje JSON recibido en makeOrder: {}", messageJson);

        try {
            ObjectMapper mapper = new ObjectMapper();
            Listdata listdata = mapper.readValue(messageJson, Listdata.class);

            if (listdata != null && listdata.getListData() != null) {
                for (DataQueue dq : listdata.getListData()) {
                    log.info("Procesando item de carrito: {}", dq);

                    try {
                        deliveryOrderService.createOrderFromDataQueue(dq);
                        log.info("Orden creada correctamente para cartItemId={}", dq.getCartItemId());
                    } catch (DeliveryOrderException e) {
                        log.error("Error creando la orden para cartItemId={}: {}", dq.getCartItemId(), e.getMessage(), e);
                    }
                }
            } else {
                log.warn("La lista de datos recibida está vacía o nula.");
            }

        } catch (Exception e) {
            log.error("Error deserializando el mensaje JSON recibido: {}", messageJson, e);
        }
    }
}
