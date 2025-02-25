package com.br.msambevorder.application.entrypoint.dto;

import com.br.msambevorder.domain.model.Order;
import com.br.msambevorder.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;

public class OrderResponse {

    private String id;
    private String ClientName;
    private List<Product> products;
    private BigDecimal totalValue;
    private String status;
    private String createdAt;
    private String externalId;

    private OrderResponse(final String id,
                          final String clientName,
                          final List<Product> products,
                          final BigDecimal totalValue,
                          final String status, String createdAt,
                          final String externalId) {
        this.id = id;
        ClientName = clientName;
        this.products = products;
        this.totalValue = totalValue;
        this.status = status;
        this.createdAt = createdAt;
        this.externalId = externalId;
    }

    public static OrderResponse of(final Order order) {

        return new OrderResponse(
                order.getId(),
                order.getClientName(),
                order.getProducts(),
                order.getTotalValue(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getExternalId());
    }

    public static OrderResponse of(final String id,
                                   final String clientName,
                                   final List<Product> products,
                                   final BigDecimal totalValue,
                                   final String status, String createdAt,
                                   final String externalId) {

        return new OrderResponse(
                id,
                clientName,
                products,
                totalValue,
                status,
                createdAt,
                externalId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
