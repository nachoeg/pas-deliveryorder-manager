package com.archpatterns.deliveryordermanager.exceptions;


import com.archpatterns.deliveryordermanager.dto.ErrorDto;
import lombok.Getter;

@Getter
public class DeliveryOrderException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final transient ErrorDto error;

    public DeliveryOrderException(ErrorDto error) {
        this.error = error;
    }
}
