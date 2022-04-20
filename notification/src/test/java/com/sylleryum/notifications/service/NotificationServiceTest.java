package com.sylleryum.notifications.service;

import com.sylleryum.common.entity.Order;
import com.sylleryum.common.util.OrderStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class NotificationServiceTest {

    @Autowired
    NotificationService notificationService;
    Order order;
    static String STOCK_ERROR = "stock error";
    static String PAYMENT_ERROR = "payment error";


    @BeforeEach
    void setUp() {
        order = new Order("itemnumber", List.of(), 1, null, null, null, null);
    }

    @Test
    void sendNotification_orderIsSuccess_noFailure() {
        order.setStockStatus(OrderStatus.SUCCESS);
        order.setPaymentStatus(OrderStatus.SUCCESS);
        List<String> failureReasons = notificationService.sendNotification(order);

        assertThat(failureReasons.isEmpty()).isTrue();
    }

    @Test
    void sendNotification_stockIsFailure_isStockError() {
        order.setStockStatus(OrderStatus.FAILED);
        order.setStockStatusReason(STOCK_ERROR);
        order.setPaymentStatus(OrderStatus.SUCCESS);
        List<String> failureReasons = notificationService.sendNotification(order);

        assertThat(failureReasons.get(0)).isEqualTo(STOCK_ERROR);
    }

    @Test
    void sendNotification_paymentIsFailure_isPaymentError() {
        order.setStockStatus(OrderStatus.SUCCESS);
        order.setPaymentStatus(OrderStatus.FAILED);
        order.setPaymentStatusReason(PAYMENT_ERROR);
        List<String> failureReasons = notificationService.sendNotification(order);

        assertThat(failureReasons.get(0)).isEqualTo(PAYMENT_ERROR);
    }

    @Test
    void sendNotification_paymentAndStockIsFailure_isPaymentErrorAndStockError() {
        order.setStockStatus(OrderStatus.FAILED);
        order.setStockStatusReason(STOCK_ERROR);
        order.setPaymentStatus(OrderStatus.FAILED);
        order.setPaymentStatusReason(PAYMENT_ERROR);
        List<String> failureReasons = notificationService.sendNotification(order);

        assertThat(failureReasons.toString()).contains(STOCK_ERROR);
        assertThat(failureReasons.toString()).contains(PAYMENT_ERROR);
    }
}