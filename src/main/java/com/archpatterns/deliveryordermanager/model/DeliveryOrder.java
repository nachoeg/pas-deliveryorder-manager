package com.archpatterns.deliveryordermanager.model;

import com.archpatterns.deliveryordermanager.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "DELIVERY_ORDER")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    @Column(name = "CREATE_TIMESTAMP")
    private LocalDateTime createTimestamp;

    @Column(name = "PICKED_TIMESTAMP")
    private LocalDateTime pickedTimeStamp;

    @Column(name = "SELLER_STARTS")
    private Integer sellerStarts;

    @Column(name = "PRODUCT_STARTS")
    private Integer productStarts;

    @Column(name = "DELIVERY_STARTS")
    private Integer deliveryStarts;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private DeliveryStatus status;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "BUYER_ID"))
    })
    private User buyer;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "DELIVER_ID"))
    })
    private User deliver;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "CARDITEM_ID")),
        @AttributeOverride(name = "productId", column = @Column(name = "CARDITEM_PRODUCT_ID")),
        @AttributeOverride(name = "quantity", column = @Column(name = "CARDITEM_QUANTITY"))
    })
    private CardItem cardItem;

    @Column(name = "PRICE_TOTAL")
    private Double priceTotal;

    @Column(name = "PRODUCT_NAME")
    private String productName;

}


