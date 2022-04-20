package com.sylleryum.payment.service;

import com.sylleryum.common.config.GlobalConfigs;
import com.sylleryum.common.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService {

    private final GlobalConfigs globalConfigs;

    public PaymentService(GlobalConfigs globalConfigs) {
        this.globalConfigs = globalConfigs;
    }

    /**
     * process payment, result will be based on {@link GlobalConfigs}
     * statusPayment (if success is set to partial, result will be random)
     * @param order
     * @return if payment was processed successfully, true, else false
     */
    public boolean newPayment(Order order){
        boolean result;
        System.out.println("******Mock payment processed "+order.getOrderPrice());
        log.debug("Mock payment processed {}",order.getOrderPrice());
        result = globalConfigs.statusPayment() == globalConfigs.SUCCESS;

        return result;
    }

    /**
     * process rollback
     * @param order
     * @return if payment was processed successfully, true, else false
     */
    public boolean rollbackPayment(Order order){
        System.out.println("******Mock payment refunded "+order.getOrderPrice());
        log.debug("Mock payment refund {}",order.getOrderPrice());
        return true;
    }
}
