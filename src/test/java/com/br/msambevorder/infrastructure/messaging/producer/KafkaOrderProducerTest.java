package com.br.msambevorder.infrastructure.messaging.producer;

import com.br.msambevorder.infrastructure.messaging.message.OrderMessage;
import com.br.msambevorder.infrastructure.util.UtilMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaOrderProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaOrderProducer kafkaOrderProducer;

    private OrderMessage orderMessage;

    @BeforeEach
    public void setup() {
        orderMessage = OrderMessage.of("1", "123", Collections.emptyList());
        ReflectionTestUtils.setField(kafkaOrderProducer, "topic", "test-topic");
    }

    @Test
    @DisplayName("Deve enviar mensagem com sucesso para o Kafka")
    public void givenValidOrderMessage_whenSendOrder_thenSendToKafka() {

        String message = UtilMapper.toJson(orderMessage);
        Set<Header> headers = Set.of(new RecordHeader("requestChildId", orderMessage.getExternalId().getBytes(StandardCharsets.UTF_8)));
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test-topic", null, System.currentTimeMillis(), null, message, headers);

        kafkaOrderProducer.sendOrder(orderMessage);

        verify(kafkaTemplate, times(1)).send(any(ProducerRecord.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao falhar no envio para o Kafka")
    public void givenKafkaSendFailure_whenSendOrder_thenThrowException() {

        String message = UtilMapper.toJson(orderMessage);
        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate).send(any(ProducerRecord.class));

        assertThrows(KafkaProducerException.class, () -> kafkaOrderProducer.sendOrder(orderMessage));
        verify(kafkaTemplate, times(1)).send(any(ProducerRecord.class));
    }

}