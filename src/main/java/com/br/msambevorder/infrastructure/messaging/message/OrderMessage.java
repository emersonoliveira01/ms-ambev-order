package com.br.msambevorder.infrastructure.messaging.message;

import com.br.msambevorder.domain.model.Product;

import java.util.List;

public class OrderMessage {

    private String orderId;
    private String externalId;
    private List<Product> products;

    private OrderMessage(String orderId, final String externalId,
                         final List<Product> products) {
        this.orderId = orderId;
        this.externalId = externalId;
        this.products = products;
    }

    public static OrderMessage of(final String orderId,
                                  final String externalId,
                                  final List<Product> products) {

         return  new OrderMessage(orderId, externalId, products);
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
