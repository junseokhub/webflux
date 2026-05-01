package com.mvc.load.order.service;

import com.mvc.load.common.exception.BusinessException;
import com.mvc.load.common.exception.ErrorCode;
import com.mvc.load.order.Order;
import com.mvc.load.order.OrderRepository;
import com.mvc.load.product.Product;
import com.mvc.load.product.ProductService;
import com.mvc.load.user.User;
import com.mvc.load.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisLuaOrderService implements OrderService{

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;

    private static final String DECREMENT_STOCK_SCRIPT = """
            local stock = tonumber(redis.call('GET', KEYS[1]))
            if stock == nil or stock <= 0 then
                return -1
            end
            redis.call('DECR', KEYS[1])
            return 1
            """;

    @Transactional
    public Order createOrder(Long userId, Long productId) {
        String stockKey = "{product:" + productId + "}:stock";

        Long result = redisTemplate.execute(
                new DefaultRedisScript<>(DECREMENT_STOCK_SCRIPT, Long.class),
                List.of(stockKey)
        );

        if (result == null || result == -1L) {
            throw new BusinessException(ErrorCode.PRODUCT_OUT_OF_STOCK);
        }

        User user = userService.findById(userId);
        Product product = productService.findById(productId);

        return orderRepository.save(Order.builder()
                .user(user)
                .product(product)
                .originalPrice(product.getPrice())
                .finalPrice(product.getPrice())
                .build());
    }
}