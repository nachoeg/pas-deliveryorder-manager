package com.archpatterns.deliveryordermanager.client;

import com.archpatterns.deliveryordermanager.dto.ChoreoData;
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
            ChoreoData choreoData = mapper.readValue(messageJson, ChoreoData.class);

            log.info("Deserialización exitosa, procesando creación de orden");

            try {
                deliveryOrderService.createOrderFromChoreoData(choreoData);
                log.info("Orden generada correctamente a partir de la cola makeOrder");
            } catch (DeliveryOrderException e) {
                log.error("Error en la creación de la orden: {}", e.getMessage(), e);
            }

        } catch (Exception e) {
            log.error("Error deserializando JSON: {}", messageJson, e);
        }
    }
}
