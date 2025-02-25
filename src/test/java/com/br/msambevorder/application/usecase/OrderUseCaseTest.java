package com.br.msambevorder.application.usecase;

import com.br.msambevorder.application.entrypoint.dto.OrderRequest;
import com.br.msambevorder.domain.model.Order;
import com.br.msambevorder.domain.model.Product;
import com.br.msambevorder.domain.repository.OrderRepository;
import com.br.msambevorder.domain.service.OrderCalculator;
import com.br.msambevorder.infrastructure.exception.DocumentOrderNotFoundException;
import com.br.msambevorder.infrastructure.exception.DuplicateOrderException;
import com.br.msambevorder.infrastructure.messaging.message.OrderMessage;
import com.br.msambevorder.infrastructure.messaging.producer.KafkaOrderProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderUseCaseTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private KafkaOrderProducer kafkaOrderProducer;

    @Mock
    private OrderCalculator orderCalculator;

    @InjectMocks
    private OrderUseCase orderUseCase;

    private OrderRequest orderRequest;

    @BeforeEach
    public void setup() {
        orderRequest = OrderRequest.of(
                "Cliente",
                List.of(
                    Product.of("012", 10, new BigDecimal(50)),
                    Product.of("011", 2, new BigDecimal(10))
                ),
                "123");
    }

    @Test
    @DisplayName("Deve lançar exceção ao encontrar pedido duplicado")
    public void givenDuplicateOrder_whenExecute_thenThrowException() {

        when(orderRepository.findByExternalId("123"))
                .thenReturn(Optional.of(
                        Order.of( "Cliente",
                                List.of(), new BigDecimal(30),
                                "PROCESSED",
                                LocalDateTime.now().toString(),
                                "123")
                ));

        var exception = assertThrows(DuplicateOrderException.class, () -> {
            orderUseCase.execute(orderRequest);
        });

        assertEquals("Este pedido já foi processado.", exception.getMessage());
        verify(orderRepository, times(1)).findByExternalId("123");
        verifyNoMoreInteractions(orderRepository, kafkaOrderProducer, orderCalculator);
    }

    @Test
    @DisplayName("Deve processar pedido com sucesso")
    public void givenValidOrder_whenExecute_thenProcessSuccessfully() {

        when(orderRepository.findByExternalId("123")).thenReturn(Optional.empty());
        when(orderCalculator.calculateTotalValue(any())).thenReturn(new BigDecimal(30));
        when(orderRepository.save(any(Order.class)))
                .thenReturn(
                        Order.of( "Cliente",
                                List.of(),
                                new BigDecimal(30),
                                "PROCESSED",
                                LocalDateTime.now().toString(),
                                "123")
                );

        var response = orderUseCase.execute(orderRequest);

        assertNotNull(response);
        assertEquals("PROCESSED", response.getStatus());
        verify(orderRepository, times(1)).findByExternalId("123");
        verify(orderCalculator, times(1)).calculateTotalValue(any());
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(kafkaOrderProducer, times(1)).sendOrder(any(OrderMessage.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando pedido não for encontrado")
    public void givenOrderNotFound_whenFindOrder_thenThrowException() {

        when(orderRepository.findById("1")).thenReturn(Optional.empty());

        var exception = assertThrows(DocumentOrderNotFoundException.class, () -> {
            orderUseCase.findOrder("1");
        });

        assertEquals("Pedido não encontrado", exception.getMessage());
        verify(orderRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Deve encontrar pedido com sucesso")
    public void givenExistingOrder_whenFindOrder_thenReturnOrder() {

        var order = Order.of(
                "Cliente", List.of(),
                new BigDecimal(30),
                "PROCESSED",
                LocalDateTime.now().toString(),
                "123"
        );
        when(orderRepository.findById("1")).thenReturn(Optional.of(order));

        var response = orderUseCase.findOrder("1");

        assertNotNull(response);
        assertEquals("123", response.getExternalId());
        verify(orderRepository, times(1)).findById("1");
    }

}