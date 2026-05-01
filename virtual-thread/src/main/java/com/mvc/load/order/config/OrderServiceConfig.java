package com.mvc.load.order.config;

import com.mvc.load.common.properties.OrderProperties;
import com.mvc.load.order.service.KafkaOrderService;
import com.mvc.load.order.service.OrderService;
import com.mvc.load.order.service.PessimisticOrderService;
import com.mvc.load.order.service.RedisLuaOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrderServiceConfig {

    private final OrderProperties orderProperties;
    private final PessimisticOrderService pessimisticOrderService;
    private final RedisLuaOrderService redisLuaOrderService;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Bean
    public OrderService orderService() {
        OrderService service = switch (orderProperties.serviceType()) {
            case PESSIMISTIC -> pessimisticOrderService;
            case REDIS_LUA -> redisLuaOrderService;
            case KAFKA -> new KafkaOrderService(kafkaTemplate, objectMapper);
        };
        log.info("OrderService 구현체: {}", service.getClass().getSimpleName());
        return service;
    }
}