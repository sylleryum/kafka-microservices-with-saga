package com.sylleryum.stock.util;

import com.sylleryum.common.config.GlobalConfigs;
import com.sylleryum.stock.entity.StockItem;
import com.sylleryum.stock.service.StockService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbDataInitializer {

    private final StockService stockService;
    private final MongoTemplate mongoTemplate;
    private final String collectionName;
    private final GlobalConfigs globalConfigs;
    private final long documentAmount;

    public DbDataInitializer(StockService stockService,
                             MongoTemplate mongoTemplate,
                             @Value("${mongodb.collection.name}") String collectionName,
                             GlobalConfigs globalConfigs, @Value("${initdb.amount}") long documentAmount) {
        this.stockService = stockService;
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;
        this.globalConfigs = globalConfigs;
        this.documentAmount = documentAmount;
    }

    public void initDbData() {
        mongoTemplate.dropCollection(collectionName);

        long itemQuantity = globalConfigs.statusStock() == globalConfigs.SUCCESS ? 99999L : 10L;
        for (int i = 1; i <= documentAmount; i++) {
            stockService.save(StockItem.builder().description("item " + i).quantity(itemQuantity).itemNumber(globalConfigs.itemPrefix() + i).build());
        }


    }
}
