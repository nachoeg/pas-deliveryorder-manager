package com.archpatterns.deliveryordermanager.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CARD_ITEM")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardItem {

    @Id
    private Long id;

    @Column(name = "PRODUCT_ID")
    private Long productId;

    @Column(name = "QUANTITY")
    private Integer quantity;

}
