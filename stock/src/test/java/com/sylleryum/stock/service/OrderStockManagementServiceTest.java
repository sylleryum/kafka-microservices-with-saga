package com.sylleryum.stock.service;

import com.sylleryum.common.config.GlobalConfigs;
import com.sylleryum.common.entity.Item;
import com.sylleryum.common.entity.Order;
import com.sylleryum.common.util.OrderStatus;
import com.sylleryum.stock.entity.StockItem;
import com.sylleryum.stock.util.DbDataInitializer;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class OrderStockManagementServiceTest {

    @Autowired
    OrderManagementService orderManagementService;
    @Autowired
    StockService stockService;
    @MockBean
    GlobalConfigs globalConfigs;
    @Autowired
    DbDataInitializer dbDataInitializer;

    @Test
    void processStockRequest_newOrder_AllItemsSuccess() {
        Mockito.when(globalConfigs.statusStock()).thenReturn(1);
        dbDataInitializer.initDbData();

        Item item = new Item(globalConfigs.itemPrefix() + 9, 5);
        Item item1 = new Item(globalConfigs.itemPrefix() + 5, 3);
        Item item2 = new Item(globalConfigs.itemPrefix() + 2, 8);
        Order order = new Order(
                "orderNumber", List.of(item, item1, item2), 3L);

        Order orderResult = orderManagementService.processStockRequest(order);
        assertThat(orderResult.getStockStatus()).isEqualTo(OrderStatus.SUCCESS
        );
    }

    @Test
    void processStockRequest_newOrder_AllItemsfailed() {
        Mockito.when(globalConfigs.statusStock()).thenReturn(-1);
        dbDataInitializer.initDbData();

        Item item = new Item(globalConfigs.itemPrefix() + 9, 5);
        Item item1 = new Item(globalConfigs.itemPrefix() + 5, 19);
        Item item2 = new Item(globalConfigs.itemPrefix() + 2, 8);
        Order order = new Order(
                "orderNumber", List.of(item, item1, item2), 3L);

        Order orderResult = orderManagementService.processStockRequest(order);
        assertThat(orderResult.getStockStatus()).isEqualTo(OrderStatus.FAILED);
    }

    @Test
    void processStockRequest_newOrder_itemNotFound() {
        Mockito.when(globalConfigs.statusStock()).thenReturn(1);
        dbDataInitializer.initDbData();

        Item item = new Item(globalConfigs.itemPrefix() + 9, 5);
        Item item1 = new Item("notFound", 3);
        Item item2 = new Item(globalConfigs.itemPrefix() + 2, 8);
        Order order = new Order(
                "orderNumber", List.of(item, item1, item2), 3L);

        Order orderResult = orderManagementService.processStockRequest(order);
        assertThat(orderResult.getStockStatus()).isEqualTo(OrderStatus.FAILED
        );
    }

    @Test
    void processStockRequest_rollback_itemQuantityIncreased() {
        Mockito.when(globalConfigs.statusStock()).thenReturn(1);
        String itemId = globalConfigs.itemPrefix() + 1;
        dbDataInitializer.initDbData();

        Item item = new Item(itemId, 11);
        Order order = new Order(
                "orderNumber", List.of(item), 3L);
        order.setStockStatus(OrderStatus.ROLLBACK);
        orderManagementService.processStockRequest(order);

        StockItem itemResult = stockService.findByItemNumbers(List.of(itemId),true).get(0);
        assertThat(itemResult.getQuantity()).isEqualTo(100010);
    }
}