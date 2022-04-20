package com.sylleryum.order.controller;

import com.sylleryum.order.entity.OrderDAO;
import com.sylleryum.order.entity.OrderPojoWrapper;
import com.sylleryum.order.util.OrderGenerator;
import com.sylleryum.common.entity.Order;
import com.sylleryum.order.producer.KafkaProducer;
import com.sylleryum.order.service.OrderService;
import com.sylleryum.order.util.OrderConverter;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1")
public class OrderController {

    private final KafkaTemplate<String, Order> kafkaTemplate;
    private final OrderService orderService;
    private final OrderConverter orderConverter;
    private final OrderGenerator orderGenerator;
    private final KafkaProducer kafkaProducer;

    public OrderController(KafkaTemplate<String, Order> kafkaTemplate,
                           OrderService orderService,
                           OrderConverter orderConverter, OrderGenerator orderGenerator, KafkaProducer kafkaProducer) {
        this.kafkaTemplate = kafkaTemplate;
        this.orderService = orderService;
        this.orderConverter = orderConverter;
        this.orderGenerator = orderGenerator;
        this.kafkaProducer = kafkaProducer;
    }

    @GetMapping("/order")
    public ResponseEntity<?> sendOrders(@RequestParam(name = "o") Optional<Long> orders,
                                        @RequestParam(name = "i") Optional<Long> items) {
        List<OrderDAO> orderDAOList = orderGenerator.generateNewOrderDAOs(orders, items);

        orderService.saveAll(orderDAOList);

        List<Order> orderList = orderConverter.listOrderDaoToKafka(orderDAOList);
        orderList.forEach(order ->
                kafkaProducer.sendOrder(order.getOrderNumber(), order));

        return ResponseEntity.ok(new OrderPojoWrapper(orderList));
    }
}
