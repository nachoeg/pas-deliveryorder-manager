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

    // Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BUYER_ID")
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DELIVER_ID")
    private User deliver;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CARD_ITEM_ID", referencedColumnName = "id")
    private CardItem cardItem;
}


