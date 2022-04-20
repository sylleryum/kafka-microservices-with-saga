package com.sylleryum.order.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sylleryum.common.util.OrderStatus;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Entity
@ToString
public class OrderDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String orderNumber;
    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ItemDAO> items;
    private double orderPrice;
    @Column(nullable = false)
    private String stockStatus = OrderStatus.NEW;
    private String stockStatusReason;
    private String paymentStatus = OrderStatus.NEW;
    private String paymentStatusReason;

    public OrderDAO() {
    }

    public OrderDAO(String orderNumber, List<ItemDAO> items, double orderPrice) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.orderPrice = orderPrice;
    }

    public OrderDAO(Long id, String orderNumber, List<ItemDAO> items, double orderPrice, String stockStatus, String stockStatusReason, String paymentStatus, String paymentStatusReason) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.items = items;
        this.orderPrice = orderPrice;
        this.stockStatus = stockStatus;
        this.stockStatusReason = stockStatusReason;
        this.paymentStatus = paymentStatus;
        this.paymentStatusReason = paymentStatusReason;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<ItemDAO> getItems() {
        return items;
    }

    public void setItems(List<ItemDAO> items) {
        this.items = items;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public String getStockStatusReason() {
        return stockStatusReason;
    }

    public void setStockStatus(String status) {
        this.stockStatus = status;
    }

    public void setStockStatusReason(String statusReason) {
        this.stockStatusReason = statusReason;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatusReason() {
        return paymentStatusReason;
    }

    public void setPaymentStatusReason(String paymentReason) {
        this.paymentStatusReason = paymentReason;
    }
}
