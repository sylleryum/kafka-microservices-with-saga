package com.sylleryum.payment.service;

import com.sylleryum.common.config.GlobalConfigs;
import com.sylleryum.common.entity.Item;
import com.sylleryum.common.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PaymentServiceTest {

    @Autowired
    PaymentService paymentService;
    @MockBean
    GlobalConfigs globalConfigs;
    Order orderTest;

    @BeforeEach
    void setUp() {
        Item item = new Item(globalConfigs.itemPrefix() + 9, 5);
        Item item1 = new Item(globalConfigs.itemPrefix() + 5, 3);
        Item item2 = new Item(globalConfigs.itemPrefix() + 2, 8);
        orderTest = new Order(
                "orderNumber", List.of(item, item1, item2), 3L);
    }

    @Test
    void newPayment_success_true() {
        Mockito.when(globalConfigs.statusPayment()).thenReturn(1);
        boolean isPaymentCompleted = paymentService.newPayment(orderTest);

        assertThat(isPaymentCompleted).isTrue();
    }

    @Test
    void newPayment_failure_true() {
        Mockito.when(globalConfigs.statusPayment()).thenReturn(-1);
        boolean isPaymentCompleted = paymentService.newPayment(orderTest);

        assertThat(isPaymentCompleted).isFalse();
    }

    @Test
    void rollbackPayment() {
        boolean rollbackPaymentResult = paymentService.rollbackPayment(orderTest);
        assertThat(rollbackPaymentResult).isTrue();
    }
}