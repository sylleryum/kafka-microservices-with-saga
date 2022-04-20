package com.sylleryum.payment.service;

import com.sylleryum.common.config.GlobalConfigs;
import com.sylleryum.common.entity.Item;
import com.sylleryum.common.entity.Order;
import com.sylleryum.common.util.OrderStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class OrderManagementServiceTest {

    @Autowired
    OrderPaymentManagementService orderManagementService;
    @MockBean
    GlobalConfigs globalConfigs;

    @Test
    void processPayment_newOrder_success() {
        Mockito.when(globalConfigs.statusPayment()).thenReturn(1);
        int itemQuantity=10;
        List<Item> itemList = new ArrayList<>();
        for (int i=1;i<=itemQuantity;i++) {
            itemList.add(new Item(globalConfigs.itemPrefix()+i, 5));
        }
        Order order = new Order(
                "orderNumber", itemList,3L);

        Order result = orderManagementService.processPayment(order);
        assertThat(result.getPaymentStatus()).isEqualTo(OrderStatus.SUCCESS);
    }

    @Test
    void processPayment_newOrder_failed() {
        Mockito.when(globalConfigs.statusPayment()).thenReturn(-1);
        int itemQuantity=10;
        List<Item> itemList = new ArrayList<>();
        for (int i=1;i<=itemQuantity;i++) {
            itemList.add(new Item(globalConfigs.itemPrefix()+i, 5));
        }
        Order order = new Order(
                "orderNumber", itemList,3L);

        Order result = orderManagementService.processPayment(order);
        assertThat(result.getPaymentStatus()).isEqualTo(OrderStatus.FAILED);
    }
}