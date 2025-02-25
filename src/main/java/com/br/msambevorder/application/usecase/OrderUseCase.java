package com.br.msambevorder.application.usecase;

import com.br.msambevorder.application.entrypoint.dto.OrderRequest;
import com.br.msambevorder.application.entrypoint.dto.OrderResponse;
import com.br.msambevorder.domain.model.Order;
import com.br.msambevorder.domain.service.OrderCalculator;
import com.br.msambevorder.infrastructure.exception.DocumentOrderNotFoundException;
import com.br.msambevorder.infrastructure.exception.DuplicateOrderException;
import com.br.msambevorder.infrastructure.messaging.message.OrderMessage;
import com.br.msambevorder.domain.repository.OrderRepository;
import com.br.msambevorder.infrastructure.messaging.producer.KafkaOrderProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderUseCase {

    private static final Logger logger = LoggerFactory.getLogger(OrderUseCase.class);
    private static final String STATUS = "PROCESSED";

    private final OrderRepository orderRepository;
    private final KafkaOrderProducer kafkaOrderProducer;
    private final OrderCalculator orderCalculator;

    public OrderUseCase(OrderRepository orderRepository,
                        KafkaOrderProducer kafkaOrderProducer,
                        OrderCalculator orderCalculator) {

        this.orderRepository = orderRepository;
        this.kafkaOrderProducer = kafkaOrderProducer;
        this.orderCalculator = orderCalculator;
    }

    @Transactional
    public OrderResponse execute(OrderRequest request) {

        var existingOrder =
                orderRepository.findByExternalId(request.getExternalId());

        if (existingOrder.isPresent()) {
            logger.warn("Pedido duplicado encontrado: {}", existingOrder.get());
            throw new DuplicateOrderException(HttpStatus.BAD_REQUEST,
                                              String.valueOf(HttpStatus.BAD_REQUEST.value()),
                                              "Este pedido já foi processado.");
        }

        var totalValue = orderCalculator.calculateTotalValue(request.getProducts());
        var order = Order.of(
                request.getClientName(),
                request.getProducts(),
                totalValue,
                STATUS,
                LocalDateTime.now().toString(),
                request.getExternalId()
        );

        var savedOrder = orderRepository.save(order);
        var orderMessage = OrderMessage.of(
                savedOrder.getId(),
                order.getExternalId(),
                order.getProducts()
        );

        logger.info("Pedido processado com sucesso: {}", savedOrder);
        kafkaOrderProducer.sendOrder(orderMessage);

        return OrderResponse.of(savedOrder);
    }

    public OrderResponse findOrder(String id) {
       return orderRepository.findById(id)
                .map(OrderResponse::of)
                .orElseThrow(() -> new DocumentOrderNotFoundException(
                            HttpStatus.NO_CONTENT,
                            String.valueOf(HttpStatus.NO_CONTENT.value()),
                            "Pedido não encontrado")
                );
    }
}
