package com.sylleryum.stock.repository;

import com.sylleryum.stock.entity.StockItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;


public interface StockRepository extends MongoRepository<StockItem, String> {


    List<StockItem>findByItemNumberIn(List<String> itemNumbers);

}
