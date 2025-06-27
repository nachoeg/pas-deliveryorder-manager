package com.archpatterns.deliveryordermanager.dto;

public record Response<T>(String mensaje, T data) {
}
