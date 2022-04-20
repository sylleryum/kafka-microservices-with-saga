package com.sylleryum.order.util;

import com.sylleryum.common.entity.Item;
import com.sylleryum.common.entity.Order;
import com.sylleryum.order.entity.OrderDAO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderConverter {

     public Order orderDaoToKafka(OrderDAO orderDAO) {
             Order orderKafka = new Order(orderDAO.getOrderNumber(),
                     orderDAO.getItems().stream().map(itemDAO ->
                             new Item(itemDAO.getItemNumber(), itemDAO.getQuantity())).collect(Collectors.toList()),
                     orderDAO.getOrderPrice(), orderDAO.getStockStatus(), orderDAO.getStockStatusReason(), orderDAO.getPaymentStatus(), orderDAO.getPaymentStatusReason());
             return orderKafka;
    }

    public List<Order> listOrderDaoToKafka(List<OrderDAO> orderDAOList){
         return orderDAOList.stream().map(this::orderDaoToKafka).collect(Collectors.toList());

    }
}
