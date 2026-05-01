package com.mvc.load.common.properties;

import com.mvc.load.order.consumer.OrderConsumerType;
import com.mvc.load.order.service.OrderServiceType;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "order")
public record OrderProperties(
        OrderServiceType serviceType,
        OrderConsumerType consumerType
) {}