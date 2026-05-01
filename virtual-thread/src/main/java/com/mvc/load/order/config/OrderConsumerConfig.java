package com.mvc.load.order.config;

import com.mvc.load.common.properties.OrderProperties;
import com.mvc.load.order.consumer.*;
import com.mvc.load.order.service.PessimisticOrderService;
import com.mvc.load.order.service.RedisLuaOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrderConsumerConfig {

    private final OrderProperties orderProperties;
    private final PessimisticOrderService pessimisticOrderService;
    private final RedisLuaOrderService redisLuaOrderService;
    private final KafkaConsumerSupport kafkaConsumerSupport;
    private final ObjectMapper objectMapper;

    @Bean
    public OrderKafkaConsumer orderKafkaConsumer() {
        if (orderProperties.consumerType() == OrderConsumerType.NONE) {
            log.info("OrderKafkaConsumer 비활성화");
            return null;
        }
        OrderKafkaConsumer consumer = switch (orderProperties.consumerType()) {
            case PESSIMISTIC -> new PessimisticOrderKafkaConsumer(
                    pessimisticOrderService, kafkaConsumerSupport, objectMapper);
            case REDIS_LUA -> new RedisLuaOrderKafkaConsumer(
                    redisLuaOrderService, kafkaConsumerSupport, objectMapper);
            default -> throw new IllegalStateException("Unexpected value: " + orderProperties.consumerType());
        };
        log.info("OrderKafkaConsumer 구현체: {}", consumer.getClass().getSimpleName());
        return consumer;
    }
}