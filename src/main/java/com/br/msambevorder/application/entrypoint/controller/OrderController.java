package com.br.msambevorder.application.entrypoint.controller;

import com.br.msambevorder.application.entrypoint.dto.OrderRequest;
import com.br.msambevorder.application.entrypoint.dto.OrderResponse;
import com.br.msambevorder.application.usecase.OrderUseCase;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;

@RestController
@RequestMapping("/orders")
@Validated
@Tag(name = "OrderController", description = "APIs para gerenciar pedidos")
public class OrderController {

    private final OrderUseCase orderUseCase;

    private static final String ORDER_SERVICE = "orderService";

    public OrderController(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @PostMapping
    @Operation(summary = "Cria um novo pedido", description = "Cria um novo pedido com base no pedido fornecido")
    @CircuitBreaker(name = ORDER_SERVICE, fallbackMethod = "fallbackCreateOrder")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
        var processedOrder = orderUseCase.execute(orderRequest);
        return ResponseEntity.ok(processedOrder);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Recupera um pedido", description = "Recupera um pedido pelo ID fornecido")
    @CircuitBreaker(name = ORDER_SERVICE, fallbackMethod = "fallbackGetOrder")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderUseCase.findOrder(id));

    }

    private ResponseEntity<OrderResponse> fallbackCreateOrder() {
        var fallbackResponse = OrderResponse.of(
                "N/A",
                "Serviço indisponível",
                Collections.emptyList(),
                BigDecimal.ZERO,
                "FALHA",
                "N/A",
                "N/A"
        );
        return ResponseEntity.status(503).body(fallbackResponse);
    }

    private ResponseEntity<OrderResponse> fallbackGetOrder() {
        var fallbackResponse = OrderResponse.of(
                "N/A",
                "Serviço indisponível",
                Collections.emptyList(),
                BigDecimal.ZERO,
                "FALHA",
                "N/A",
                "N/A"
        );
        return ResponseEntity.status(503).body(fallbackResponse);
    }
}
