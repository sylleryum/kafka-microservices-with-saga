package com.sylleryum.order.service;

import com.sylleryum.order.entity.ItemDAO;
import com.sylleryum.order.entity.OrderDAO;
import com.sylleryum.order.exception.OrderException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    OrderService orderService;
    static OrderDAO orderDAO;
    static ItemDAO itemDAO;

    static {
        System.setProperty("spring.kafka.streams.state-dir", "/tmp/kafka-streams/" + UUID.randomUUID());
    }

    @BeforeAll
    static void beforeAll() {
        itemDAO = ItemDAO.builder().itemNumber(UUID.randomUUID().toString()).build();
        orderDAO = new OrderDAO(UUID.randomUUID().toString(), List.of(itemDAO), 1.99);
        itemDAO.setOrder(orderDAO);

    }

    @Test
    void save_persistOrder_persisted() {
        OrderDAO save = orderService.save(orderDAO);

        assertThat(save).isNotNull();

    }

    @Test
    void findByOrderNumber_isFound() {
        String orderNumberTest = "order1";
        orderDAO.setOrderNumber(orderNumberTest);
        orderService.save(orderDAO);

        Optional<OrderDAO> orderFound = orderService.findByOrderNumber(orderNumberTest);
        assertThat(orderFound.get().getId()).isNotNull();
    }

    @Test
    void findByOrderNumber_notFound_optionalIsEmpty() {
        String orderNumberNotfound = "notfound";

        Optional<OrderDAO> orderResult = orderService.findByOrderNumber(orderNumberNotfound);
        assertThat(orderResult.isPresent()).isFalse();
    }
}