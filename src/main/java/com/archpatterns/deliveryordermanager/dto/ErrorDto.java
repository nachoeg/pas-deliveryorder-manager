package com.archpatterns.deliveryordermanager.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;
    private String detail;
    private String message;
    private String localizedException;
}
