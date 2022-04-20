package com.sylleryum.stock.service;

import com.sylleryum.stock.entity.StockItem;
import com.sylleryum.stock.exceptions.StockException;
import com.sylleryum.stock.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StockService {

    private final StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public List<StockItem> findAll() {
        return stockRepository.findAll();
    }
    public <S extends StockItem> S save(S entity) {
        return stockRepository.save(entity);
    }

    public <S extends StockItem> List<S> saveAll(Iterable<S> entities) {
        return stockRepository.saveAll(entities);
    }

    public long count() {
        return stockRepository.count();
    }

    /**
     * find {@link StockItem} based on itemNumbers
     * @param itemNumbers
     * @param matchAll if true, returns list of {@link StockItem} only if all itemNumbers passed are found, else, returns an empty list
     * @return
     */
    public List<StockItem> findByItemNumbers(List<String> itemNumbers, boolean matchAll) {
        if (itemNumbers.isEmpty()) {
            log.error("no item number passed");
            throw new StockException("no Item number passed");
        }
        List<StockItem> itemList = stockRepository.findByItemNumberIn(itemNumbers);
        if (!matchAll) return itemList;

        if (itemList.size()==itemNumbers.size()) return itemList;
        log.debug("no StockItem found for itemBumbers {}", itemNumbers);
        return List.of();
    }


}
