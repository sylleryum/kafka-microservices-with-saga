package com.sylleryum.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:shared.properties")
public class GlobalConfigs {

    private final int statusStock;
    private final int statusPayment;
    private final String itemPrefix;
    public final int SUCCESS = 1;
    public final int FAILURE = -1;

    public GlobalConfigs(@Value("${order.status.stock}") int statusStock,
                         @Value("${order.status.payment}") int statusPayment,
                         @Value("${item.number.prefix}") String itemPrefix) {
        this.statusStock = statusStock;
        this.statusPayment = statusPayment;
        this.itemPrefix = itemPrefix;
        ;
    }

    public int statusStock() {
        return statusStock;
    }

    public int statusPayment() {
        return statusPayment;
    }

    public String itemPrefix() {
        return itemPrefix;
    }
}
