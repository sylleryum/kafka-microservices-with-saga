package com.sylleryum.order.service;

import com.sylleryum.order.entity.OrderDAO;
import com.sylleryum.order.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDAO save(OrderDAO orderDAO){
        return orderRepository.save(orderDAO);
    }

    public <S extends OrderDAO> Iterable<S> saveAll(Iterable<S> entities) {
        return orderRepository.saveAll(entities);
    }

    public Optional<OrderDAO> findByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber);
    }
}
