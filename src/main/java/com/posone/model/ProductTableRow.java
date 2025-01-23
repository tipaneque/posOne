package com.posone.model;

public class ProductTableRow {
    private String id;
    private String name;
    private float price;
    private int quantity;
    private float total;

    public ProductTableRow(String id, String name, float price, int quantity, float total) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
