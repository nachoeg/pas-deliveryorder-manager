package com.archpatterns.deliveryordermanager.service;

import com.archpatterns.deliveryordermanager.dto.ChoreoData;
import com.archpatterns.deliveryordermanager.dto.DeliveryOrderRequest;
import com.archpatterns.deliveryordermanager.dto.DeliveryOrderResponse;
import com.archpatterns.deliveryordermanager.dto.ErrorDto;
import com.archpatterns.deliveryordermanager.enums.DeliveryStatus;
import com.archpatterns.deliveryordermanager.exceptions.DeliveryOrderException;
import com.archpatterns.deliveryordermanager.model.CardItem;
import com.archpatterns.deliveryordermanager.model.DeliveryOrder;
import com.archpatterns.deliveryordermanager.model.User;
import com.archpatterns.deliveryordermanager.repository.DeliveryOrderRepository;
import com.archpatterns.deliveryordermanager.utils.Util;
import com.archpatterns.deliveryordermanager.service.JwtService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DeliveryOrderServiceImpl implements DeliveryOrderService {

    @Autowired
    private DeliveryOrderRepository deliveryOrderRepository;

    @Autowired
    private JwtService jwtService;

    @Override
    public DeliveryOrderResponse createOrder(DeliveryOrderRequest request) throws DeliveryOrderException {

        var buyer = User.builder()
                .id(request.getBuyerId())
                .build();

        User deliver = null;

        var cardItem = CardItem.builder()
                .id(request.getCardItemId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();

        var deliveryOrder = DeliveryOrder.builder()
                .buyer(buyer)
                .deliver(deliver)
                .status(DeliveryStatus.PENDING_ORDER)
                .cardItem(cardItem)
                .priceTotal(request.getPriceTotal())
                .productName(request.getProductName())
                .build();

        deliveryOrder = deliveryOrderRepository.save(deliveryOrder);

        return Util.convertToResponse(deliveryOrder);
    }

    @Override
    public void createOrderFromChoreoData(ChoreoData data) throws DeliveryOrderException {
        log.debug("Procesando choreoData recibido: {}", data);

        var buyer = User.builder()
                .id(data.getBuyerId())
                .build();

        User deliver = null;

        var cardItem = CardItem.builder()
                .id(data.getCartItemId())
                .productId(data.getProductId())
                .quantity(data.getQuantity())
                .build();

        var deliveryOrder = DeliveryOrder.builder()
                .buyer(buyer)
                .deliver(deliver)
                .status(DeliveryStatus.PENDING_ORDER)
                .cardItem(cardItem)
                .priceTotal(data.getPriceTotal())
                .productName(data.getProductName())
                .build();

        deliveryOrderRepository.save(deliveryOrder);

        log.info("Orden creada correctamente con datos extendidos (priceTotal y productName)");
    }

    @Override
    public List<DeliveryOrderResponse> getOrdersByBuyer(Long buyerId) throws DeliveryOrderException {
        return deliveryOrderRepository.findAllByBuyerId(buyerId)
                .stream()
                .map(Util::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryOrderResponse> getOrdersByDeliver(Long deliverId) throws DeliveryOrderException {
        return deliveryOrderRepository.findAllByDeliverId(deliverId)
                .stream()
                .map(Util::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryOrderResponse> getOrdersByStatus(DeliveryStatus status) throws DeliveryOrderException {
        return deliveryOrderRepository.findAllByStatus(status)
                .stream()
                .map(Util::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryOrderResponse pickOrder(Long deliveryOrderId, Long deliverId) throws DeliveryOrderException {
        var order = deliveryOrderRepository.findById(deliveryOrderId)
                .orElseThrow(() -> Util.notFound("Orden no encontrada", deliveryOrderId));

        var deliver = User.builder()
                .id(deliverId)
                .build();

        order.setDeliver(deliver);
        order.setStatus(DeliveryStatus.PICKEDUP_ORDER);
        order.setPickedTimeStamp(LocalDateTime.now());

        return Util.convertToResponse(deliveryOrderRepository.save(order));
    }

    @Override
    public DeliveryOrderResponse deliverOrder(Long deliveryOrderId, Long userId, String[] roles) throws DeliveryOrderException {
        var order = deliveryOrderRepository.findById(deliveryOrderId)
                .orElseThrow(() -> Util.notFound("Orden no encontrada", deliveryOrderId));

        boolean isAdmin = List.of(roles).contains("ADMIN");
        boolean isDeliverAssigned = order.getDeliver() != null && order.getDeliver().getId().equals(userId);

        if (!isAdmin && !isDeliverAssigned) {
            throw new DeliveryOrderException(ErrorDto.builder()
                    .code(403)
                    .message("No tiene permisos para marcar la orden como entregada")
                    .build());
        }

        order.setStatus(DeliveryStatus.DELIVERED_ORDER);
        return Util.convertToResponse(deliveryOrderRepository.save(order));
    }

    @Override
    public DeliveryOrderResponse cancelOrder(Long deliveryOrderId, Long userId, String[] roles) throws DeliveryOrderException {
        var order = deliveryOrderRepository.findById(deliveryOrderId)
                .orElseThrow(() -> Util.notFound("Orden no encontrada", deliveryOrderId));

        boolean isAdmin = List.of(roles).contains("ADMIN");
        boolean isDeliverAssigned = order.getDeliver() != null && order.getDeliver().getId().equals(userId);

        if (!isAdmin && !isDeliverAssigned) {
            throw new DeliveryOrderException(ErrorDto.builder()
                    .code(403)
                    .message("No tiene permisos para cancelar la orden")
                    .build());
        }

        order.setStatus(DeliveryStatus.CANCELLED_ORDER);
        return Util.convertToResponse(deliveryOrderRepository.save(order));
    }

    @Override
    public DeliveryOrderResponse qualifyOrder(Long deliveryOrderId, int productStars, int sellerStars, int deliveryStars, Long userId, String[] roles) throws DeliveryOrderException {
        var order = deliveryOrderRepository.findById(deliveryOrderId)
                .orElseThrow(() -> Util.notFound("Orden no encontrada", deliveryOrderId));

        boolean isAdmin = List.of(roles).contains("ADMIN");
        boolean isBuyerAssigned = order.getBuyer() != null && order.getBuyer().getId().equals(userId);

        if (!isAdmin && !isBuyerAssigned) {
            throw new DeliveryOrderException(ErrorDto.builder()
                    .code(403)
                    .message("No tiene permisos para calificar esta orden")
                    .build());
        }

        order.setProductStarts(productStars);
        order.setSellerStarts(sellerStars);
        order.setDeliveryStarts(deliveryStars);

        return Util.convertToResponse(deliveryOrderRepository.save(order));
    }

    @Override
    public Long extractUserIdFromCurrentRequest() throws DeliveryOrderException {
        var authHeader = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new DeliveryOrderException(ErrorDto.builder()
                    .code(400)
                    .message("Falta el token de autorización")
                    .build());
        }

        var token = authHeader.substring(7);
        return jwtService.extractUserId(token);
    }

    @Override
    public String[] extractUserRolesFromCurrentRequest() throws DeliveryOrderException {
        var authHeader = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest()
                .getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new DeliveryOrderException(ErrorDto.builder()
                    .code(400)
                    .message("Falta el token de autorización")
                    .build());
        }

        var token = authHeader.substring(7);
        return jwtService.extractRoles(token);
    }
}
