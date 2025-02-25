package com.br.msambevorder.application.entrypoint.controller;

import com.br.msambevorder.application.entrypoint.dto.OrderRequest;
import com.br.msambevorder.application.entrypoint.dto.OrderResponse;
import com.br.msambevorder.application.usecase.OrderUseCase;
import com.br.msambevorder.domain.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderUseCase orderUseCase;

    private OrderResponse orderResponse;

    @BeforeEach
    public void setup() {

        orderResponse =  OrderResponse.of(
                "1",
                "Cliente",
                List.of(
                        Product.of("012", 10, BigDecimal.valueOf(50)),
                        Product.of("011", 2, BigDecimal.valueOf(10))
                ),
                BigDecimal.valueOf(50),
                "PROCESSED",
                LocalDateTime.now().toString(),
                "123"
        );

        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderUseCase)).build();
    }

    @Test
    @DisplayName("Deve criar um pedido com sucesso")
    public void givenValidOrderRequest_whenCreateOrder_thenReturnStatusOk() throws Exception {

        when(orderUseCase.execute(any(OrderRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"externalId\":\"123\",\"clientName\":\"Cliente\",\"products\":[{\"name\":\"Produto1\",\"price\":10.0,\"quantity\":1},{\"name\":\"Produto2\",\"price\":20.0,\"quantity\":1}]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.clientName").value("Cliente"));
    }

    @Test
    @DisplayName("Deve retornar um pedido com sucesso")
    public void givenValidOrderId_whenGetOrder_thenReturnOrder() throws Exception {

        when(orderUseCase.findOrder(eq("1"))).thenReturn(orderResponse);

        mockMvc.perform(get("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.clientName").value("Cliente"));
    }

}