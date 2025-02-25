package com.br.msambevorder.infrastructure.persistence;

import com.br.msambevorder.domain.model.Order;
import com.br.msambevorder.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class OrderMongoRepository implements OrderRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public Order save(Order order) {
        return mongoTemplate.save(order);
    }

    @Override
    public Optional<Order> findById(String id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Order.class));
    }

    @Override
    public Optional<Order> findByExternalId(String externalId) {
        return Optional.ofNullable(mongoTemplate.findOne(
                Query.query(Criteria.where("externalId").is(externalId)),
                Order.class));
    }
}
