# Microservices with Kafka and Saga pattern using Spring boot
This project simulates a system for processing orders (i.e., a purchase) of items (E.g.: an order of a fridge and a camera) which is constituted of 4 microservices: order, stock, payment and notification service.
## High level architecture:
<p align="center">
<img src="https://raw.githubusercontent.com/sylleryum/kafka-microservices-with-saga/main/resources/readme-images/architecture.png" alt="" width="50%"/>
</p>

**Note:** as the main objective of this project is to demonstrate Kafka, majority of microservice patterns are ignored as well as some best practices for simplicity/readability’s sake (E.g.: Transactional outbox and coding to the interface).

## Installing / Getting started:
-	Clone this repo.
-	Run the docker compose file inside “docker files” directory (docker-compose up), if mongo-express fails to initialize, simply re-run it.
-	Run all the microservices (any order of initialization is fine).
-	Send orders through order service’s endpoint /api/v1/order specifying the amount of orders to send and the amount of items inside each order through query param o (order) and i (item), E.g.: localhost:8080/api/v1/order?o=2&i=3 will send 2 orders, each order contains 3 items within itself.
-	You can easily check the final result of each order through notification service’s console or check each topic through kafkdrop (localhost:9000/).
-	You can change the expected order result (success/failure) and other configurations through shared.properties inside common module.

## How it works:
Once a new order is received, the order service does the initial processing and sends a new event to kafka:
<p align="center">
<img src="https://raw.githubusercontent.com/sylleryum/kafka-microservices-with-saga/main/resources/readme-images/step1.png" alt="" width="50%"/>
</p>
All microservices involved in the order will perform their corresponding operations and send a confirmation back to Kafka (success/failure):
<p align="center">
<img src="https://raw.githubusercontent.com/sylleryum/kafka-microservices-with-saga/main/resources/readme-images/step2.png" alt="" width="50%"/>
</p>
Order service then uses Kafka Streams to join all the confirmations received (inner join). If all services returned a success event, order has been fully processed (order completed). If any service returns a failure message, order service then triggers an event of rollback which will be processed by all other services. 
Order service also sends the final order status to Kafka, notification service simulates then a notification message to user informing the final status of his/her order:
<p align="center">
<img src="https://raw.githubusercontent.com/sylleryum/kafka-microservices-with-saga/main/resources/readme-images/step3.png" alt="" width="50%"/>
</p>
