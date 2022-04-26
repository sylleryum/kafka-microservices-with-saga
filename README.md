# Microservices with Kafka and Saga pattern using Spring boot
This project simulates a system for processing orders (i.e., a purchase) of items (E.g.: an order of a fridge and a camera) which is constituted of 4 microservices: order, stock, payment and notification service.
## High level architecture:
<p align="center">
<img src="https://raw.githubusercontent.com/sylleryum/kafka-microservices-with-saga/main/resources/readme-images/architecture.png" alt="" width="50%"/>
</p>

**Note:** as the main objective of this project is to demonstrate Kafka, majority of microservice patterns are ignored as well as some best practices for simplicity/readability’s sake (E.g.: Transactional outbox and coding to the interface).

## Getting started / Installation:

### Option 1: Running locally
- Clone this repo.
- Run the docker compose file inside “resources/docker files/Run project locally” directory (docker-compose up), if mongo-express fails to initialize, simply re-run it.
- Run all the microservices (any order of initialization is fine).

### Option 2: Running on Docker
- Simply run the docker compose file inside “resources/docker files/Run project on docker” directory (docker-compose up), if mongo-express fails to initialize, simply re-run it.

## Instructions:

- Send orders through order service’s endpoint /api/v1/order specifying the amount of orders to send and the amount of items inside each order through query param o (order) and i (item). 
  - E.g.: localhost:8080/api/v1/order?o=2&i=3 will send 2 orders, each order contains 3 items within itself.
- You can easily check the final result of each order through notification service’s console or check each topic through kafkdrop (localhost:9000/).
- You can change the expected order result (success/failure) and other configurations through shared.properties inside common module.

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

## Configurations:
- If running locally, configurations of the microservices can be changed through shared.application located at common\src\resources\ (explanaition of relevant configurations are included in this file).
- 
## Considerations regarding this project and best practices:
- Kafka may be tricky to handle proficiently duplications/idempotency. In this project the approach of [enabling idempotent producer was used](https://docs.confluent.io/platform/current/installation/configuration/producer-configs.html#producerconfigs_enable.idempotence). Consumer and its commit offset strategy should be considered also. E.g.: [Idempotent Kafka Consumer](https://medium.com/techwasti/idempotent-kafka-consumer-442f9aec991e)
- This projects uses 2 DBs, Postgres for Order service and MongoDB for Stock service, this is only to showcase microservices and Kafka with different DBs.
- Common module should be replaced in a real scenario for a better approach as externalized configuration pattern.
- For simplicity sake, Kafka producer/consumer are using a shared entity located in the common module, in a real scenario Avro/schema registry (included in the docker-compose file) is advised.
- A caveat of using Kafka Streams and inner join to process the results of an order being processed by order services is that it is time windowed, if for any reason a service takes longer than the window time to answer an order, the orchestrator will never process the confirmation of the order. A few alternatives is to use individual listeners in the orchestrator or outer join and schedule a task to verify the order after join window has closed.
- If a rollback is needed, orchestrator (order service) will send an event (order) that is consumed by all services involved in processing an order rather than individual rollback events (E.g.: rollback event to payment service) as usually maximum 1 service will fail and because of this, all other services will have to rollback and therefore, consume an event. 