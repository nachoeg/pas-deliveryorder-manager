package com.archpatterns.deliveryordermanager.handler;

import com.archpatterns.deliveryordermanager.dto.ErrorDto;
import com.archpatterns.deliveryordermanager.exceptions.DeliveryOrderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class Handler {

    @ExceptionHandler(DeliveryOrderException.class)
    public ResponseEntity<ErrorDto> restClientError(DeliveryOrderException ex) {
        log.error("Handler Error: {} ", ex.getError().getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(ex.getError().getCode())).body(ex.getError());
    }
}
