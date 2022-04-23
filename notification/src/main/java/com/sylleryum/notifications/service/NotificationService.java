package com.sylleryum.notifications.service;

import com.sylleryum.common.entity.Order;
import com.sylleryum.common.util.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationService.class);

    public List<String> sendNotification(Order order) {
        if (order.getStockStatus().equalsIgnoreCase(OrderStatus.SUCCESS) && order.getPaymentStatus().equalsIgnoreCase(OrderStatus.SUCCESS)) {
            successNotification(order);
            return List.of();
        }
        return failureNotification(order);
    }

    private void successNotification(Order order) {
        LOG.info("success for order {}", order.getOrderNumber());
        System.out.println("***Notification sent: your order " + order.getOrderNumber() + " has been processed and is ready to be shipped...");
    }

    private List<String> failureNotification(Order order) {
        List<String> failureReasons = new ArrayList<>();
        LOG.warn("Order failed {}", order);
        if (order.getStockStatus().equalsIgnoreCase(OrderStatus.FAILED))
            failureReasons.add(order.getStockStatusReason());
        if (order.getPaymentStatus().equalsIgnoreCase(OrderStatus.FAILED)) {
            failureReasons.add(order.getPaymentStatusReason());
        }
        String singularOrPluralReason = failureReasons.size() > 1 ? "reasons" : "reason";

        System.out.println("*********Order " + order.getOrderNumber() + " has failed for the following " + singularOrPluralReason + " :" + failureReasons);
        return failureReasons;
    }
}
