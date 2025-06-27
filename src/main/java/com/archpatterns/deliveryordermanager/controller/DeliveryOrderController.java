package com.archpatterns.deliveryordermanager.controller;

import com.archpatterns.deliveryordermanager.dto.*;
import com.archpatterns.deliveryordermanager.enums.DeliveryStatus;
import com.archpatterns.deliveryordermanager.exceptions.DeliveryOrderException;
import com.archpatterns.deliveryordermanager.service.DeliveryOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("deliveryorder-manager/api/orders")
@Tag(name = "Orden de Entrega")
@RequiredArgsConstructor
public class DeliveryOrderController {

    private final DeliveryOrderService deliveryOrderService;


    @Operation(summary = "Crear nueva orden de entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOrderResponse> createOrder(@RequestBody DeliveryOrderRequest request) {
        return ResponseEntity.ok(deliveryOrderService.createOrder(request));
    }


    @Operation(summary = "Obtener órdenes por comprador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping(path = "/buyer/{buyer_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryOrderResponse>> getOrdersByBuyer(@PathVariable Long buyer_id) throws DeliveryOrderException {
        return ResponseEntity.ok(deliveryOrderService.getOrdersByBuyer(buyer_id));
    }

    @Operation(summary = "Obtener órdenes por repartidor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping(path = "/deliver/{delivery_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryOrderResponse>> getOrdersByDeliver(@PathVariable Long delivery_id) throws DeliveryOrderException {
        return ResponseEntity.ok(deliveryOrderService.getOrdersByDeliver(delivery_id));
    }

    @Operation(summary = "Obtener órdenes por estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryOrderResponse>> getOrdersByStatus(@RequestParam DeliveryStatus status) throws DeliveryOrderException {
        return ResponseEntity.ok(deliveryOrderService.getOrdersByStatus(status));
    }

    @Operation(summary = "Asignar orden a repartidor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    @PatchMapping(path = "/{delivery_order_id}/pick", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOrderResponse> pickOrder(@PathVariable Long delivery_order_id,
                                                           @RequestParam Long deliver_id) throws DeliveryOrderException {
        return ResponseEntity.ok(deliveryOrderService.pickOrder(delivery_order_id, deliver_id));
    }

    @Operation(summary = "Marcar orden como entregada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    @PatchMapping(path = "/{delivery_order_id}/deliver", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOrderResponse> deliverOrder(@PathVariable Long delivery_order_id) throws DeliveryOrderException {
        return ResponseEntity.ok(deliveryOrderService.deliverOrder(delivery_order_id));
    }

    @Operation(summary = "Cancelar orden de entrega")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    @PatchMapping(path = "/{delivery_order_id}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOrderResponse> cancelOrder(@PathVariable Long delivery_order_id) throws DeliveryOrderException {
        return ResponseEntity.ok(deliveryOrderService.cancelOrder(delivery_order_id));
    }

    @Operation(summary = "Calificar orden")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping(path = "/{delivery_order_id}/qualify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOrderResponse> qualifyOrder(@PathVariable Long delivery_order_id,
                                                              @RequestBody QualifyRequest request) throws DeliveryOrderException {
        return ResponseEntity.ok(deliveryOrderService.qualifyOrder(
                delivery_order_id,
                request.getProductStarts(),
                request.getSellerStarts(),
                request.getDeliveryStarts()
        ));
    }

    @Data
    public static class QualifyRequest {
        private int productStarts;
        private int sellerStarts;
        private int deliveryStarts;
    }
}
