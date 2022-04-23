package com.sylleryum.order.service;

import com.sylleryum.common.entity.Order;
import com.sylleryum.common.util.OrderStatus;
import com.sylleryum.order.entity.OrderDAO;
import com.sylleryum.order.exception.OrderException;
import com.sylleryum.order.producer.KafkaProducer;
import com.sylleryum.order.util.OrderConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class OrderManagementService {

    private final OrderService orderService;
    private final OrderConverter orderConverter;
    private final KafkaProducer kafkaProducer;

    public OrderManagementService(OrderService orderService, OrderConverter orderConverter, KafkaProducer kafkaProducer) {
        this.orderService = orderService;
        this.orderConverter = orderConverter;
        this.kafkaProducer = kafkaProducer;
    }

    @Transactional
    public Order processOrder(Order stockOrder, Order paymentOrder) {
            OrderDAO orderDAO = orderService.findByOrderNumber(stockOrder.getOrderNumber()).orElseThrow(() -> {
                log.error("something is wrong, order {} should be available but isn't, " +
                        "if here, put a breakpoint to check it",stockOrder.getOrderNumber());
                return new OrderException("order not found");
            });
            orderDAO.setStockStatus(stockOrder.getStockStatus());
            orderDAO.setStockStatusReason(stockOrder.getStockStatusReason());
            orderDAO.setPaymentStatus(paymentOrder.getPaymentStatus());
            orderDAO.setPaymentStatusReason(paymentOrder.getPaymentStatusReason());
            orderService.save(orderDAO);

            //where notifications can be triggered to user informing the final status of the order
            kafkaProducer.sendNotification(orderDAO.getOrderNumber(), orderConverter.orderDaoToKafka(orderDAO));

            if (orderDAO.getStockStatus().equalsIgnoreCase(OrderStatus.SUCCESS) &&
                    orderDAO.getPaymentStatus().equalsIgnoreCase(OrderStatus.SUCCESS)) {
                log.debug("order succeeded {}", orderDAO);
                return orderConverter.orderDaoToKafka(orderDAO);
            }
            //if arrived here, at least one service failed
            if (orderDAO.getStockStatus().equalsIgnoreCase(OrderStatus.FAILED)) {
                //both failed
                if (orderDAO.getPaymentStatus().equalsIgnoreCase(OrderStatus.FAILED)) {
                    log.debug("rollback order, both failed {}", orderDAO);
                    orderDAO.setStockStatus(OrderStatus.ROLLBACK);
                    orderDAO.setPaymentStatus(OrderStatus.ROLLBACK);
                    return orderConverter.orderDaoToKafka(orderDAO);
                }
                //only stockOrder failed
                log.debug("rollback order, only stockOrder failed {}", orderDAO);
                orderDAO.setPaymentStatus(OrderStatus.ROLLBACK);
                return orderConverter.orderDaoToKafka(orderDAO);
            }

            //if arrived here, only payment failed
            log.debug("rollback order, only payment failed {}", orderDAO);
            orderDAO.setStockStatus(OrderStatus.ROLLBACK);
            return orderConverter.orderDaoToKafka(orderDAO);
    }

}
