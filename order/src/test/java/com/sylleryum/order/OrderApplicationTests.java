package com.sylleryum.order;

import com.sylleryum.common.entity.Order;
import com.sylleryum.order.service.OrderService;
import com.sylleryum.order.util.OrderConverter;
import com.sylleryum.order.util.OrderGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;


@SpringBootTest
@Slf4j
class OrderApplicationTests {

    @Autowired
    OrderService orderService;
    @Autowired
    KafkaTemplate<String, Order> kafkaTemplate;
    @Autowired
    OrderConverter orderConverter;
    @Autowired
    OrderGenerator orderGenerator;

    @Test
    @Ignore
    void generalTests() {
        log.debug("info");
        log.debug("debug");
        log.trace("trace");
        log.warn("warn");
    }

}
