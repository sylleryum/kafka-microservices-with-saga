package com.sylleryum.order.util;

import com.sylleryum.common.config.GlobalConfigs;
import com.sylleryum.order.entity.ItemDAO;
import com.sylleryum.order.entity.OrderDAO;
import com.sylleryum.order.exception.OrderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest
class OrderGeneratorTest {

    @Autowired
    OrderGenerator orderGenerator;
    @MockBean
    GlobalConfigs globalConfigs;

    @BeforeEach
    void setUp() {
        Mockito.when(globalConfigs.itemPrefix()).thenReturn("itemnumber");
    }

    @Test
    void generateNewOrderDAOs_allOrdersGenerated_true() {
        var order = Optional.of(3L);
        var item = Optional.of(5L);

        List<OrderDAO> orderList = orderGenerator.generateNewOrderDAOs(order, item);
        assertThat(orderList.size()).isEqualTo(3);
    }

    @Test
    void generateNewOrderDAOs_globalConfigFailure_quantityAbove10() {
        Mockito.when(globalConfigs.statusStock()).thenReturn(-1);
        var order = Optional.of(3L);
        var item = Optional.of(5L);

        List<OrderDAO> orderList = orderGenerator.generateNewOrderDAOs(order, item);
        List<Long> itemQuantities = orderList.stream()
                .flatMap(orderDAO -> orderDAO.getItems().stream())
                .map(ItemDAO::getQuantity)
                .collect(Collectors.toList());
        itemQuantities.forEach(
                itemQuantity -> assertThat(itemQuantity).isGreaterThan(10));
    }

    @Test
    void generateNewOrderDAOs_itemNumbersAreInSequence_true() {
//        var order = Optional.of(5L);
//        var item = Optional.of(3L);
//        long lastItemNumber = (order.get() * item.get());
//
//        List<OrderDAO> orderList = orderGenerator.generateNewOrderDAOs(order, item);
//
//        assertThat(orderList.get(4).getItems().get(2).getItemNumber()).isEqualTo(globalConfigs.itemPrefix()+lastItemNumber);
    }

    @Test
    void generateNewOrderDAOs_maxAmountInvalid_throwsOrderException() {
        var order = Optional.of(2L);
        var item = Optional.of(501L);

        Assertions.assertThrows(OrderException.class,() ->
                orderGenerator.generateNewOrderDAOs(order, item));
    }
}