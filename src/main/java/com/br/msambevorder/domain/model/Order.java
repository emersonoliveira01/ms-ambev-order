package com.br.msambevorder.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "order")
public class Order {

    @Id
    private String id;
    private String clientName;
    private List<Product> products;
    private BigDecimal totalValue;
    private String status;
    private String createdAt;

    @Indexed(unique = true)
    private String externalId;

    private Order(final String id, String clientName,
                  final List<Product> products,
                  final BigDecimal totalValue,
                  final String status,
                  final String createdAt,
                  final String externalId) {

        this.id = id;
        this.clientName = clientName;
        this.products = products;
        this.totalValue = totalValue;
        this.status = status;
        this.createdAt = createdAt;
        this.externalId =  externalId;
    }

    public static Order of(final String clientName,
                           final List<Product> products,
                           final BigDecimal totalValue,
                           final String status,
                           final String creationDate,
                           final String externalId) {

        return new Order(null,
                clientName,
                products,
                totalValue,
                status,
                creationDate, externalId);

    }

    public String getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public String getStatus() {
        return status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getExternalId() {
        return externalId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", clientName='" + clientName + '\'' +
                ", products=" + products +
                ", totalValue=" + totalValue +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", externalId='" + externalId + '\'' +
                '}';
    }
}
