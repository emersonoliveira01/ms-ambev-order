package com.br.msambevorder.infrastructure.messaging.producer;

import com.br.msambevorder.infrastructure.messaging.message.OrderMessage;
import com.br.msambevorder.infrastructure.util.UtilMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaProducerException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;

@Service
public class KafkaOrderProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaOrderProducer.class);
    private static final String REQUEST_CHILD_ID = "requestChildId";

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaOrderProducer(KafkaTemplate<String, String> kafkaTemplate,
                              @Value("${topic.name.producer}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendOrder(final OrderMessage orderMessage) {
        String message = UtilMapper.toJson(orderMessage);
        Set<Header> headers = createHeaders(orderMessage.getExternalId());

        ProducerRecord<String, String> producerRecord = createProducerRecord(message, headers);
        sendToKafka(producerRecord);

        logger.info("Order enviado ao produto external: {}", message);
    }

    private Set<Header> createHeaders(String externalId) {
        Header header = new RecordHeader(REQUEST_CHILD_ID, externalId.getBytes(StandardCharsets.UTF_8));
        return Collections.singleton(header);
    }

    private ProducerRecord<String, String> createProducerRecord(String message, Set<Header> headers) {
        return new ProducerRecord<>(topic, null, System.currentTimeMillis(), null, message, headers);
    }

    private void sendToKafka(final ProducerRecord<String, String> producerRecord) {
        try {
            kafkaTemplate.send(producerRecord);
        } catch (Exception e) {
            logger.error("Error sending to Kafka", e);
            throw new KafkaProducerException(producerRecord, "Error sending message", e);
        }
    }
}
