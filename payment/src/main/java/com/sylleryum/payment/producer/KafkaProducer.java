package com.sylleryum.payment.producer;

import com.sylleryum.common.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer {


    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final String kafkaTopicPayment;


    private static final Logger LOG = LoggerFactory.getLogger(KafkaProducer.class);

    public KafkaProducer(KafkaTemplate<String, Order> kafkaTemplate,
                         @Value("${topic.name.payment}") String kafkaTopicPayment) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTopicPayment = kafkaTopicPayment;
    }

    public void send(String key, Order payload) {
        log.debug("sending payload={}, key={}, topic={}", payload, key, kafkaTopicPayment);
        kafkaTemplate.send(kafkaTopicPayment, key, payload);
    }


}
