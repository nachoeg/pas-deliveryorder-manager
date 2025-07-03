package com.archpatterns.deliveryordermanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataQueue implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	private Long cartItemId;
	private Long prodId;
	private Long buyerId;
	private Long sellerId;
	private Long walletId;
	private Integer quantity;
	private Double priceTotal;
	private String prodName;
}

