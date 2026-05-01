package com.mvc.load.order.service;

import com.mvc.load.order.Order;

public interface OrderService {
    Order createOrder(Long userId, Long productId);
}