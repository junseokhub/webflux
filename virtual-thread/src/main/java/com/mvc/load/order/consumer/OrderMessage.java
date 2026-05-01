package com.mvc.load.order.consumer;

public record OrderMessage(
        Long userId,
        Long productId,
        String correlationId
) {
}
