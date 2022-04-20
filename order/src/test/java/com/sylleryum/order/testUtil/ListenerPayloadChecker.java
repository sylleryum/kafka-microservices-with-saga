package com.sylleryum.order.testUtil;

import com.sylleryum.common.entity.Order;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * a simple listener with the purpose to check the payload received
 */
@Component
public class ListenerPayloadChecker {

    private CountDownLatch latch = new CountDownLatch(1);
    private ConsumerRecord<String, Order> consumerRecord = null;

    @KafkaListener(topics = "${topic.name.order}", groupId = "test")
    private void receive(ConsumerRecord<String, Order> consumerRecord) {
        setConsumerRecord(consumerRecord);
        latch.countDown();
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public ConsumerRecord<String, Order> getConsumerRecord() {
        return consumerRecord;
    }

    public void setConsumerRecord(ConsumerRecord<String, Order> consumerRecord) {
        this.consumerRecord = consumerRecord;
    }
}
