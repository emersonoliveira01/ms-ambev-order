package com.br.msambevorder.application.entrypoint.dto;

import com.br.msambevorder.domain.model.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class OrderRequest {

    private String clientName;
    private List<Product> products;

    @NotBlank(message = "External ID é obrigatório")
    @NotNull
    private String externalId;

    public OrderRequest(final String clientName,
                        final List<Product> products,
                        final String externalId) {

        this.clientName = clientName;
        this.products = products;
        this.externalId = externalId;
    }

    public static OrderRequest of(final String clientName,
                                  final List<Product> products,
                                  final String externalId) {

        return new OrderRequest(clientName, products, externalId);

    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
