package com.mvc.load.order.consumer;

import com.mvc.load.order.service.RedisLuaOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
public class RedisLuaOrderKafkaConsumer implements OrderKafkaConsumer {

    private final RedisLuaOrderService redisLuaOrderService;
    private final KafkaConsumerSupport support;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-requests", groupId = "mvc-order-consumer-group")
    public void consume(String message) {
        try {
            OrderMessage orderMessage = objectMapper.readValue(message, OrderMessage.class);
            processOrder(orderMessage);
        } catch (Exception e) {
            log.error("order-requests 처리 실패: {}", e.getMessage());
        }
    }

    private void processOrder(OrderMessage orderMessage) {
        try {
            redisLuaOrderService.createOrder(
                    orderMessage.userId(),
                    orderMessage.productId()
            );
            support.sendResult(orderMessage.correlationId());
        } catch (Exception e) {
            support.handleError(e, orderMessage);
        }
    }
}