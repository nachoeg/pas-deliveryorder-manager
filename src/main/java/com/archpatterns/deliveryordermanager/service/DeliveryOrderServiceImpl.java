package com.archpatterns.deliveryordermanager.service;

import com.archpatterns.deliveryordermanager.dto.DeliveryOrderRequest;
import com.archpatterns.deliveryordermanager.dto.DeliveryOrderResponse;
import com.archpatterns.deliveryordermanager.enums.DeliveryStatus;
import com.archpatterns.deliveryordermanager.model.CardItem;
import com.archpatterns.deliveryordermanager.model.DeliveryOrder;
import com.archpatterns.deliveryordermanager.model.User;
import com.archpatterns.deliveryordermanager.repository.CardItemRepository;
import com.archpatterns.deliveryordermanager.repository.DeliveryOrderRepository;
import com.archpatterns.deliveryordermanager.repository.UserRepository;
import com.archpatterns.deliveryordermanager.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryOrderServiceImpl implements DeliveryOrderService {

    @Autowired
    private DeliveryOrderRepository deliveryOrderRepository;

    @Autowired
    private CardItemRepository cardItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public DeliveryOrderResponse createOrder(DeliveryOrderRequest request) {
        User buyer = new User();
        buyer.setId(request.getBuyerId());
    
        User deliver = null;
        if (request.getDeliveryId() != null) {
            deliver = new User();
            deliver.setId(request.getDeliveryId());
        }
    
        CardItem cardItem = cardItemRepository.findById(request.getCardItemId())
            .orElseThrow(() -> Util.notFound("Ítem no encontrado", request.getCardItemId()));
    
        DeliveryOrder deliveryOrder = DeliveryOrder.builder()
                .buyer(buyer)
                .deliver(deliver)
                .status(request.getStatus() != null ? request.getStatus() : DeliveryStatus.PENDING_ORDER)
                .cardItem(cardItem)
                .build();
    
        deliveryOrder = deliveryOrderRepository.save(deliveryOrder);
    
        return Util.convertToResponse(deliveryOrder);
    }

    @Override
    public List<DeliveryOrderResponse> getOrdersByBuyer(Long buyerId) {
        return deliveryOrderRepository.findAllByBuyer_Id(buyerId)
                .stream()
                .map(Util::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryOrderResponse> getOrdersByDeliver(Long deliveryId) {
        return deliveryOrderRepository.findAllByDeliver_Id(deliveryId)
                .stream()
                .map(Util::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<DeliveryOrderResponse> getOrdersByStatus(DeliveryStatus status) {
        return deliveryOrderRepository.findAllByStatus(status)
                .stream()
                .map(Util::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DeliveryOrderResponse pickOrder(Long deliveryOrderId, Long deliverId) {
        DeliveryOrder order = deliveryOrderRepository.findById(deliveryOrderId)
                .orElseThrow(() -> Util.notFound("Orden no encontrada", deliveryOrderId));

        User deliver = new User();
        deliver.setId(deliverId);

        order.setDeliver(deliver);
        order.setStatus(DeliveryStatus.PICKEDUP_ORDER);
        return Util.convertToResponse(deliveryOrderRepository.save(order));
    }

    @Override
    public DeliveryOrderResponse deliverOrder(Long deliveryOrderId) {
        DeliveryOrder order = deliveryOrderRepository.findById(deliveryOrderId)
                .orElseThrow(() -> Util.notFound("Orden no encontrada", deliveryOrderId));

        order.setStatus(DeliveryStatus.DELIVERED_ORDER);
        return Util.convertToResponse(deliveryOrderRepository.save(order));
    }

    @Override
    public DeliveryOrderResponse cancelOrder(Long deliveryOrderId) {
        DeliveryOrder order = deliveryOrderRepository.findById(deliveryOrderId)
                .orElseThrow(() -> Util.notFound("Orden no encontrada", deliveryOrderId));

        order.setStatus(DeliveryStatus.CANCELLED_ORDER);
        return Util.convertToResponse(deliveryOrderRepository.save(order));
    }

    @Override
    public DeliveryOrderResponse qualifyOrder(Long deliveryOrderId, int productStars, int sellerStars, int deliveryStars) {
        DeliveryOrder order = deliveryOrderRepository.findById(deliveryOrderId)
                .orElseThrow(() -> Util.notFound("Orden no encontrada", deliveryOrderId));

        order.setProductStarts(productStars);
        order.setSellerStarts(sellerStars);
        order.setDeliveryStarts(deliveryStars);

        return Util.convertToResponse(deliveryOrderRepository.save(order));
    }
}
