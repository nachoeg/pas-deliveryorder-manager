package com.archpatterns.deliveryordermanager.model;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardItem {
    private Long id;
    private Long productId;
    private Integer quantity;
}