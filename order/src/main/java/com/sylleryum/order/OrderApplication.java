package com.sylleryum.order;

import com.sylleryum.common.entity.Order;
import com.sylleryum.order.service.OrderManagementService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.UUID;

@SpringBootApplication
@ComponentScan(basePackages = "com.sylleryum")
@EnableKafkaStreams
@Slf4j
public class OrderApplication {

    static {
        System.setProperty("spring.kafka.streams.state-dir", "/tmp/kafka-streams/"+ UUID.randomUUID());
    }
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class, args);
    }

    private final String kafkaTopicStock;
    private final String kafkaTopicPayment;
    private final String kafkaTopicOrder;
    private final long joinWindow;
    private final OrderManagementService orderManagementService;


    public OrderApplication(@Value("${topic.name.stock}") String kafkaTopicStock,
                            @Value("${topic.name.payment}") String kafkaTopicPayment,
                            @Value("${topic.name.order}") String kafkaTopicOrder,
                            @Value("${kafka.join.window.duration.ms}") long joinWindow, OrderManagementService orderManagementService) {
        this.kafkaTopicPayment = kafkaTopicPayment;
        this.kafkaTopicStock = kafkaTopicStock;
        this.kafkaTopicOrder = kafkaTopicOrder;
        this.joinWindow = joinWindow;
        this.orderManagementService = orderManagementService;
    }


    @Bean
    public KStream<String, Order> kstreamOrder(StreamsBuilder builder) {
        Serde<String> stringSerde = Serdes.String();
        JsonSerde<Order> orderJsonSerde = new JsonSerde<>(Order.class);

        KStream<String, Order> orderStockStream = builder.stream(kafkaTopicStock,
                Consumed.with(stringSerde, orderJsonSerde));
        KStream<String, Order> orderPaymentStream = builder.stream(kafkaTopicPayment,
                Consumed.with(stringSerde, orderJsonSerde));


        orderStockStream.join(orderPaymentStream, orderManagementService::processOrder, JoinWindows.of(Duration.ofMillis(joinWindow)),
                        StreamJoined.with(stringSerde, orderJsonSerde, orderJsonSerde))
                .peek((s, entityJoin) -> log.debug("order joined: {}",entityJoin))
                .to(kafkaTopicOrder);

        return orderStockStream;
    }



}
