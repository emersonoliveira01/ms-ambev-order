package com.br.msambevorder.domain.service;

import com.br.msambevorder.domain.model.Product;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderCalculator {

    /**
     * Calcula o valor total de uma lista de produtos.
     *
     * @param products a lista de produtos para calcular o valor total
     * @return o valor total como BigDecimal
     */
    public BigDecimal calculateTotalValue(List<Product> products) {
        return products.stream()
                .map(product -> product.getPrice()
                        .multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
