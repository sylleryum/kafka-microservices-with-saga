package com.sylleryum.stock;

import com.sylleryum.common.entity.Order;
import com.sylleryum.stock.service.OrderManagementService;
import com.sylleryum.stock.service.StockService;
import com.sylleryum.stock.util.DbDataInitializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@ComponentScan(basePackages = "com.sylleryum")
@EnableKafka
@Slf4j
public class StockApplication implements CommandLineRunner {

    private final DbDataInitializer dbDataInitializer;
    private final StockService stockService;
    private final OrderManagementService orderManagementService;

    public StockApplication(DbDataInitializer dbDataInitializer, StockService stockService, OrderManagementService orderManagementService) {
        this.dbDataInitializer = dbDataInitializer;
        this.stockService = stockService;
        this.orderManagementService = orderManagementService;
    }


    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
    }

    @Override
    public void run(String... args) {
        dbDataInitializer.initDbData();
    }

    @KafkaListener(topics = "${topic.name.order}")
    private void listener(Order order) {

        orderManagementService.processStockRequest(order);
    }

}
