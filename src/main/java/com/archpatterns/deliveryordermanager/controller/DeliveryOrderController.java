package com.archpatterns.deliveryordermanager.controller;

import com.archpatterns.deliveryordermanager.dto.*;
import com.archpatterns.deliveryordermanager.enums.DeliveryStatus;
import com.archpatterns.deliveryordermanager.exceptions.DeliveryOrderException;
import com.archpatterns.deliveryordermanager.service.DeliveryOrderService;
import com.archpatterns.deliveryordermanager.service.JwtService;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
@RestController
@RequestMapping("deliveryorder-manager/api/orders")
@Tag(name = "Orden de Entrega")
@RequiredArgsConstructor
public class DeliveryOrderController {

    private final DeliveryOrderService deliveryOrderService;

    @Operation(summary = "Crear nueva orden de entrega")
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOrderResponse> createOrder(@RequestBody DeliveryOrderRequest request) {
        return ResponseEntity.ok(deliveryOrderService.createOrder(request));
    }

    @Operation(summary = "Obtener órdenes del comprador autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping(path = "/buyer", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryOrderResponse>> getOrdersByBuyer() throws DeliveryOrderException {
        Long userId = deliveryOrderService.extractUserIdFromCurrentRequest();
        return ResponseEntity.ok(deliveryOrderService.getOrdersByBuyer(userId));
    }

    @Operation(summary = "Obtener órdenes de cualquier comprador (sólo admins)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping(path = "/buyer/{buyer_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryOrderResponse>> getOrdersByBuyerAdmin(@PathVariable Long buyer_id) throws DeliveryOrderException {
        return ResponseEntity.ok(deliveryOrderService.getOrdersByBuyer(buyer_id));
    }

    @Operation(summary = "Obtener órdenes del repartidor autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping(path = "/deliver", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryOrderResponse>> getOrdersByDeliver() throws DeliveryOrderException {
        Long userId = deliveryOrderService.extractUserIdFromCurrentRequest();
        return ResponseEntity.ok(deliveryOrderService.getOrdersByDeliver(userId));
    }

    @Operation(summary = "Obtener órdenes de cualquier repartidor (sólo admins)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping(path = "/deliver/{delivery_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryOrderResponse>> getOrdersByDeliverAdmin(@PathVariable Long delivery_id) throws DeliveryOrderException {
        return ResponseEntity.ok(deliveryOrderService.getOrdersByDeliver(delivery_id));
    }

    @Operation(summary = "Obtener órdenes por estado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeliveryOrderResponse>> getOrdersByStatus(@RequestParam DeliveryStatus status) throws DeliveryOrderException {
        return ResponseEntity.ok(deliveryOrderService.getOrdersByStatus(status));
    }

    @Operation(summary = "Asignar orden a repartidor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PatchMapping(path = "/{delivery_order_id}/pick", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOrderResponse> pickOrder(@PathVariable Long delivery_order_id) throws DeliveryOrderException {
        Long deliverId = deliveryOrderService.extractUserIdFromCurrentRequest();
        return ResponseEntity.ok(deliveryOrderService.pickOrder(delivery_order_id, deliverId));
    }

    @Operation(summary = "Marcar orden como entregada")
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PatchMapping(path = "/{delivery_order_id}/deliver", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOrderResponse> deliverOrder(@PathVariable Long delivery_order_id) throws DeliveryOrderException {
        Long userId = deliveryOrderService.extractUserIdFromCurrentRequest();
        String[] roles = deliveryOrderService.extractUserRolesFromCurrentRequest();
        return ResponseEntity.ok(deliveryOrderService.deliverOrder(delivery_order_id, userId, roles));
    }

    @Operation(summary = "Cancelar orden de entrega")
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PatchMapping(path = "/{delivery_order_id}/cancel", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOrderResponse> cancelOrder(@PathVariable Long delivery_order_id) throws DeliveryOrderException {
        Long userId = deliveryOrderService.extractUserIdFromCurrentRequest();
        String[] roles = deliveryOrderService.extractUserRolesFromCurrentRequest();
        return ResponseEntity.ok(deliveryOrderService.cancelOrder(delivery_order_id, userId, roles));
    }

    @Operation(summary = "Calificar orden")
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "500", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping(path = "/{delivery_order_id}/qualify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeliveryOrderResponse> qualifyOrder(@PathVariable Long delivery_order_id,
                                                              @RequestBody QualifyRequest request) throws DeliveryOrderException {
        Long userId = deliveryOrderService.extractUserIdFromCurrentRequest();
        String[] roles = deliveryOrderService.extractUserRolesFromCurrentRequest();
        return ResponseEntity.ok(deliveryOrderService.qualifyOrder(
                delivery_order_id,
                request.getProductStarts(),
                request.getSellerStarts(),
                request.getDeliveryStarts(),
                userId,
                roles
        ));
    }

    @Data
    public static class QualifyRequest {
        private int productStarts;
        private int sellerStarts;
        private int deliveryStarts;
    }
}
