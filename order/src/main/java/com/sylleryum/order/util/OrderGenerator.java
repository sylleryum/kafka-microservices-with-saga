package com.sylleryum.order.util;

import com.sylleryum.order.entity.ItemDAO;
import com.sylleryum.order.entity.OrderDAO;
import com.sylleryum.common.config.GlobalConfigs;
import com.sylleryum.order.exception.OrderException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class OrderGenerator {

    private final GlobalConfigs globalConfigs;
    private final long maxItemsAllowed;
    private long currentItemNumber=1;

    public OrderGenerator(GlobalConfigs globalConfigs,
                          @Value("${max.items.allowed}") long maxItemsAllowed) {
        this.globalConfigs = globalConfigs;
        this.maxItemsAllowed = maxItemsAllowed;
    }

    /**
     * Generate new orderDAOs (to be persisted), quantity of item inside each order is determined by {@link GlobalConfigs}
     * @param amountOrders how many orders. Default = 5
     * @param amountItems how many different items inside each order (for each different item the quantity is determined by {@link GlobalConfigs}. Default = 4
     * @return List of {@link OrderDAO} ready to be persisted
     */
    public List<OrderDAO> generateNewOrderDAOs(Optional<Long> amountOrders, Optional<Long> amountItems) throws OrderException{
        long totalItemAmount = amountOrders.orElse(1L) * amountItems.orElse(1L);
        if (totalItemAmount > maxItemsAllowed){
            throw new OrderException("amount of total items exceed the amount allowed. " +
                    "Total amount passed: "+totalItemAmount+ " max amount allowed : "+maxItemsAllowed);
        }
        List<OrderDAO> orderDAOList = new ArrayList<>();

        for (int i = 0; i < amountOrders.orElse(5L); i++) {
            OrderDAO orderDAO = new OrderDAO(UUID.randomUUID().toString(),
                    null,
                    Math.floor(Math.random() * (1000 - 1 + 1) + 1));

            orderDAO.setItems(generateNewItemDAOs(amountItems.orElse(4L), orderDAO));
            orderDAOList.add(orderDAO);
        }

        return orderDAOList;
    }

    /**
     * generates list of {@link ItemDAO} to be included inside an {@link OrderDAO}
     * @param amountItems how many items to be generated
     * @param orderDAO the order to be linked with this list
     * @return
     */
    private List<ItemDAO> generateNewItemDAOs (Long amountItems, OrderDAO orderDAO) {
        List<ItemDAO> itemDaoList = new ArrayList<>();

        for (int j = 0; j < amountItems; j++) {
            itemDaoList.add(ItemDAO.builder()
                    .itemNumber(globalConfigs.itemPrefix()+currentItemNumber)
                    .quantity(generateQuantity())
                    .order(orderDAO)
                    .build());
            currentItemNumber++;
        }

        return itemDaoList;
    }

    /**
     * used to determine the quantity of different items inside an order based on {@link GlobalConfigs} (e.g. all items should have success to reserve?).
     * all items in stock microservice have 10 units, therefore quantities above this will fail.
     * @return number representing the quantity of an item inside an order (e.g. an order of 16 microwaves)
     */
    private int generateQuantity() {
        if (globalConfigs.statusStock() == globalConfigs.SUCCESS) {
            return (int) Math.floor(Math.random() * (10 - 1 + 1) + 1);
        } else if (globalConfigs.statusStock() == globalConfigs.FAILURE) {
            return (int) Math.floor(Math.random() * (20 - 11 + 1) + 11);
        }
        //if config is invalid, return success,
        // could be configured differently (e.g. throw exception for no configuration found)
        return (int) Math.floor(Math.random() * (10 - 1 + 1) + 1);
    }
}
