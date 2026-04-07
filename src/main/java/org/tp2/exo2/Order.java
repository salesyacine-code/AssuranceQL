// src/main/java/com/example/model/Order.java
package org.tp2.exo2;

public class Order {
    private long id;
    private String product;
    private int quantity;
    private double price;

    public Order(long id, String product, int quantity, double price) {
        this.id       = id;
        this.product  = product;
        this.quantity = quantity;
        this.price    = price;
    }

    public long getId()       { return id; }
    public String getProduct(){ return product; }
    public int getQuantity()  { return quantity; }
    public double getPrice()  { return price; }

    @Override
    public String toString() {
        return "Order{id=" + id + ", product='" + product +
                "', quantity=" + quantity + ", price=" + price + "}";
    }
}