package com.br.msambevorder.domain.service;

import com.br.msambevorder.domain.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderCalculatorTest {

    private final OrderCalculator orderCalculator = new OrderCalculator();

    @Test
    @DisplayName("Deve calcular o valor total de uma lista de produtos corretamente")
    public void givenValidProductList_whenCalculateTotalValue_thenReturnCorrectTotal() {

       var products = List.of(Product.of("1", 10, BigDecimal.valueOf(2)),
                              Product.of("2",20, BigDecimal.valueOf(1)));

       var totalValue = orderCalculator.calculateTotalValue(products);

        assertEquals(BigDecimal.valueOf(40), totalValue);
    }

    @Test
    @DisplayName("Deve retornar zero ao calcular uma lista vazia de produtos")
    public void givenEmptyProductList_whenCalculateTotalValue_thenReturnZero() {

        var totalValue = orderCalculator.calculateTotalValue(List.of());

        assertEquals(BigDecimal.ZERO, totalValue);
    }

    @Test
    @DisplayName("Deve lidar corretamente com produtos de valor zero")
    public void givenProductWithZeroValue_whenCalculateTotalValue_thenReturnCorrectTotal() {

        var products = List.of(Product.of("1", 0, BigDecimal.valueOf(5)));

        var totalValue = orderCalculator.calculateTotalValue(products);

        assertEquals(BigDecimal.ZERO, totalValue);
    }

    @Test
    @DisplayName("Deve lidar com produtos de quantidade zero")
    public void givenProductWithZeroQuantity_whenCalculateTotalValue_thenReturnCorrectTotal() {

        var products = List.of(Product.of("1", 10, BigDecimal.valueOf(0)));

        var totalValue = orderCalculator.calculateTotalValue(products);

        assertEquals(BigDecimal.ZERO, totalValue);
    }

}