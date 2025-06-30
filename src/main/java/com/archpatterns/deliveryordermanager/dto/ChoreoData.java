package com.archpatterns.deliveryordermanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChoreoData {
    private Long cartItemId;
    private Long productId;
    private Integer quantity;
    private Double priceTotal;
    private String productName;
    private Long buyerId;
    private Long sellerId;
    private Long walletId;
}
