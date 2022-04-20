package com.sylleryum.common.entity;

public class Item {

    private String itemNumber;
    private long quantity;

    public Item() {
    }

    public Item(String itemNumber, long quantity) {
        this.itemNumber = itemNumber;
        this.quantity = quantity;
    }

    public String getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }


    @Override
    public String toString() {
        return "Item{" +
                "itemNumber='" + itemNumber + '\'' +
                ", quantity=" + quantity +
                '}';
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
}
