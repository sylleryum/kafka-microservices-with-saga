package com.sylleryum.order.service;

import com.sylleryum.common.entity.Order;
import com.sylleryum.common.util.OrderStatus;
import com.sylleryum.order.entity.OrderDAO;
import com.sylleryum.order.producer.KafkaProducer;
import com.sylleryum.order.util.OrderConverter;
import com.sylleryum.order.util.OrderGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class OrderManagementServiceTest {

    @MockBean
    OrderService orderService;
    @MockBean
    KafkaProducer kafkaProducer;
    @Autowired
    OrderConverter orderConverter;
    @Autowired
    OrderGenerator orderGenerator;
    @Autowired
    OrderManagementService orderManagementService;


    @Test
    void processOrder_allSuccess_true() {
        OrderDAO orderDAO = orderGenerator.generateNewOrderDAOs(Optional.of(1L), Optional.of(1L)).get(0);
        orderDAO.setStockStatus(OrderStatus.SUCCESS);
        orderDAO.setPaymentStatus(OrderStatus.SUCCESS);
        Order order = orderConverter.orderDaoToKafka(orderDAO);

        Mockito.when(orderService.save(orderDAO)).thenReturn(orderDAO);
        Mockito.when(orderService.findByOrderNumber(orderDAO.getOrderNumber())).thenReturn(Optional.of(orderDAO));
        Order orderResult = orderManagementService.processOrder(order, order);

        assertThat(orderResult.getStockStatus()).isEqualTo(OrderStatus.SUCCESS);
        assertThat(orderResult.getPaymentStatus()).isEqualTo(OrderStatus.SUCCESS);
    }

    @Test
    void processOrder_allFailed_allRollback() {
        OrderDAO orderDAO = orderGenerator.generateNewOrderDAOs(Optional.of(1L), Optional.of(1L)).get(0);
        orderDAO.setStockStatus(OrderStatus.FAILED);
        orderDAO.setPaymentStatus(OrderStatus.FAILED);
        Order order = orderConverter.orderDaoToKafka(orderDAO);

        Mockito.when(orderService.save(orderDAO)).thenReturn(orderDAO);
        Mockito.when(orderService.findByOrderNumber(orderDAO.getOrderNumber())).thenReturn(Optional.of(orderDAO));
        Order orderResult = orderManagementService.processOrder(order, order);

        assertThat(orderResult.getStockStatus()).isEqualTo(OrderStatus.ROLLBACK);
        assertThat(orderResult.getPaymentStatus()).isEqualTo(OrderStatus.ROLLBACK);
    }

    @Test
    void processOrder_stockFailed_paymentRollback() {
        OrderDAO orderDAO = orderGenerator.generateNewOrderDAOs(Optional.of(1L), Optional.of(1L)).get(0);
        orderDAO.setStockStatus(OrderStatus.FAILED);
        orderDAO.setPaymentStatus(OrderStatus.SUCCESS);
        Order order = orderConverter.orderDaoToKafka(orderDAO);

        Mockito.when(orderService.save(orderDAO)).thenReturn(orderDAO);
        Mockito.when(orderService.findByOrderNumber(orderDAO.getOrderNumber())).thenReturn(Optional.of(orderDAO));
        Order orderResult = orderManagementService.processOrder(order, order);

        assertThat(orderResult.getStockStatus()).isEqualTo(OrderStatus.FAILED);
        assertThat(orderResult.getPaymentStatus()).isEqualTo(OrderStatus.ROLLBACK);
    }

    @Test
    void processOrder_paymentFailed_stockRollback() {
        OrderDAO orderDAO = orderGenerator.generateNewOrderDAOs(Optional.of(1L), Optional.of(1L)).get(0);
        orderDAO.setStockStatus(OrderStatus.SUCCESS);
        orderDAO.setPaymentStatus(OrderStatus.FAILED);
        Order order = orderConverter.orderDaoToKafka(orderDAO);

        Mockito.when(orderService.save(orderDAO)).thenReturn(orderDAO);
        Mockito.when(orderService.findByOrderNumber(orderDAO.getOrderNumber())).thenReturn(Optional.of(orderDAO));
        Order orderResult = orderManagementService.processOrder(order, order);

        assertThat(orderResult.getStockStatus()).isEqualTo(OrderStatus.ROLLBACK);
        assertThat(orderResult.getPaymentStatus()).isEqualTo(OrderStatus.FAILED);
    }
}