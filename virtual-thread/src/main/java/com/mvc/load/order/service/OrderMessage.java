package com.mvc.load.order.service;

public record OrderMessage(
        Long userId,
        Long productId,
        String correlationId
) {
}
