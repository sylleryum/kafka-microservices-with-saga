package com.sylleryum.stock.service;

import com.sylleryum.common.config.GlobalConfigs;
import com.sylleryum.stock.entity.StockItem;
import com.sylleryum.stock.exceptions.StockException;
import com.sylleryum.stock.util.DbDataInitializer;

import org.junit.Ignore;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StockServiceTest {

    @Autowired
    StockService stockService;
    @Value("${initdb.amount}")
    long documentAmount;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    GlobalConfigs globalConfigs;

    @Test
    @Ignore
    void test() {
        mongoTemplate.dropCollection("stock");
//        currentStockItems.sort(Comparator.comparing(StockItem::getDescription));
        StockItem stockItem = stockService.save(StockItem.builder().description("item test").quantity(10L).itemNumber("it").build());
        StockItem stockItem1 = stockService.save(StockItem.builder().description("item c").quantity(10L).itemNumber("it").build());
        StockItem stockItem2 = stockService.save(StockItem.builder().description("item g").quantity(10L).itemNumber("it").build());
        List<StockItem> all = stockService.findAll();
//        all.sort(Comparator.comparing(StockItem::getDescription));
        System.out.println();
    }

    @BeforeAll
    static void beforeAll(@Autowired DbDataInitializer dbDataInitializer) {
        dbDataInitializer.initDbData();
    }

    @Test
    @Order(1)
    void count_equalDocumentAmount_true() {
        long count = stockService.count();
        assertThat(count).isEqualTo(documentAmount);
    }

    @Test
    @Order(2)
    void findAll_equalDocumentAmount_true() {
        List<StockItem> all = stockService.findAll();
        assertThat(all.size()).isEqualTo(documentAmount);

    }

    @Test
    void findByItemNumbers_allFound_true() {
        String item1 = globalConfigs.itemPrefix() + "3";
        String item2 = globalConfigs.itemPrefix() + "9";
        List<StockItem> itemNumbers = stockService.findByItemNumbers(List.of(item1, item2), true);
        assertThat(itemNumbers.size()).isEqualTo(2);
    }

    @Test
    void findByItemNumbers_allFound_false() {
        String item1 = globalConfigs.itemPrefix() + "3";
        String item2 = globalConfigs.itemPrefix() + "Nope";
        List<StockItem> itemNumbers = stockService.findByItemNumbers(List.of("i3", "nope"), true);
        assertThat(itemNumbers.isEmpty()).isTrue();
    }

    @Test
    void findByItemNumbers_noArgsPassed_throwsStockException(){

        Assertions.assertThrows(StockException.class, () ->
                stockService.findByItemNumbers(List.of(), false));
    }

    @Test
    void save_persistEntity_idCreated() {
        StockItem stockItem = stockService.save(StockItem.builder().description("item test").quantity(10L).itemNumber("it").build());
        assertThat(stockItem.getId()).isNotEmpty();

    }
}