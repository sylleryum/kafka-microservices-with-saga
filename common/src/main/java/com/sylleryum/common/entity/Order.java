package com.sylleryum.common.entity;


import com.sylleryum.common.util.OrderStatus;

import java.util.List;

public class Order {

    private String orderNumber;
    private List<Item> items;
    private double orderPrice;
    private String stockStatus = OrderStatus.NEW;
    private String stockStatusReason;
    private String paymentStatus = OrderStatus.NEW;
    private String paymentStatusReason;
    //TODO change constructors for a builder
    public Order() {
    }

    public Order(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Order(String orderNumber, double orderPrice) {
        this.orderNumber = orderNumber;
        this.orderPrice = orderPrice;
    }

    public Order(String orderNumber, List<Item> items, double orderPrice) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.orderPrice = orderPrice;
    }

    public Order(String orderNumber, List<Item> items, double orderPrice, String stockStatus, String stockStatusReason, String paymentStatus, String paymentStatusReason) {
        this.orderNumber = orderNumber;
        this.items = items;
        this.orderPrice = orderPrice;
        this.stockStatus = stockStatus;
        this.stockStatusReason = stockStatusReason;
        this.paymentStatus = paymentStatus;
        this.paymentStatusReason = paymentStatusReason;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
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

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public String getStockStatusReason() {
        return stockStatusReason;
    }

    public void setStockStatusReason(String stockStatusReason) {
        this.stockStatusReason = stockStatusReason;
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

    public void setPaymentStatusReason(String paymentStatusReason) {
        this.paymentStatusReason = paymentStatusReason;
    }


    @Override
    public String toString() {
        return "Order{" +
                "orderNumber='" + orderNumber + '\'' +
                ", items=" + items +
                ", orderPrice=" + orderPrice +
                ", stockStatus='" + stockStatus + '\'' +
                ", stockStatusReason='" + stockStatusReason + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentStatusReason='" + paymentStatusReason + '\'' +
                '}';
    }
}
