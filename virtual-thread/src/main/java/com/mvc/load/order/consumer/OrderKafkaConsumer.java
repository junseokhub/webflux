package com.mvc.load.order.consumer;

public interface OrderKafkaConsumer {
    void consume(String message);
}
