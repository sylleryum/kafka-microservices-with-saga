package com.sylleryum.stock.entity;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "stock")
public class StockItem {

    private String id;
    private String itemNumber;
    @Indexed(unique = true)
    private String description;
    private long quantity;

}
