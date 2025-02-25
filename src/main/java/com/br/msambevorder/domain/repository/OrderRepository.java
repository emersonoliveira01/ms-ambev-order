package com.br.msambevorder.domain.repository;

import com.br.msambevorder.domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);
    Optional<Order> findById(String id);
    Optional<Order> findByExternalId(String externalId);
}
