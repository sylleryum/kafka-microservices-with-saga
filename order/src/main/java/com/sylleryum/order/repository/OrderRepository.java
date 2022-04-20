package com.sylleryum.order.repository;

import com.sylleryum.order.entity.OrderDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface OrderRepository extends CrudRepository<OrderDAO, Long> {

    @Transactional
    Optional<OrderDAO> findByOrderNumber(String orderNumber);

}
