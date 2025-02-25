package com.br.msambevorder.domain.model;

import java.math.BigDecimal;

public class Product {

    private String id;
    private int quantity;
    private BigDecimal price;

    private Product(final String id,
                    final int quantity,
                    final BigDecimal price) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
    }

    public static Product of(final String id,
                             final int quantity,
                             final BigDecimal price) {

        return new Product(id, quantity, price);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
