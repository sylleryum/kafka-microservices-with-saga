package com.sylleryum.order.util;

import com.sylleryum.common.entity.Order;
import com.sylleryum.order.entity.ItemDAO;
import com.sylleryum.order.entity.OrderDAO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class OrderConverterTest {

    @Autowired
    OrderConverter orderConverter;

    @Test
    void orderPojoToKafka_convertOrderDAOtoOrder_newOrder() {
        //create orderDAO
        ItemDAO itemDAO = ItemDAO.builder().itemNumber(UUID.randomUUID().toString()).id(1L).build();
        OrderDAO orderDAO = new OrderDAO(UUID.randomUUID().toString(), List.of(itemDAO),1.99);
        itemDAO.setOrder(orderDAO);

        //convert orderDAO to order from common package
        Order order = orderConverter.orderDaoToKafka(orderDAO);

        assertThat(order).isNotNull();
        assertThat(order).isInstanceOf(Order.class);
    }

    @Test
    void listOrderPojoToKafka_convertListOfOrderDAOtoOrder_newListOfOrder() {
        List<OrderDAO> listOrderDAO = new ArrayList<>();
        ItemDAO itemDAO = ItemDAO.builder().itemNumber(UUID.randomUUID().toString()).id(1L).build();
        OrderDAO orderDAO = new OrderDAO(UUID.randomUUID().toString(), List.of(itemDAO),1.99);
        itemDAO.setOrder(orderDAO);
        listOrderDAO.add(orderDAO);

        List<Order> listOrder = orderConverter.listOrderDaoToKafka(listOrderDAO);

        //size of order
        assertThat(listOrder.size()).isEqualTo(1);
        //size of items inside order
        assertThat(listOrder.get(0).getItems().size()).isEqualTo(1);
    }
}