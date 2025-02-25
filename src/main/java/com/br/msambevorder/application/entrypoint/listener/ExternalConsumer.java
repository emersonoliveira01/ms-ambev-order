package com.br.msambevorder.application.entrypoint.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ExternalConsumer {

    private static final Logger logger = LoggerFactory.getLogger(ExternalConsumer.class);
    private static final String ORDER_TOPIC = "order-topic";
    private static final String ORDER_SERVICE_GROUP = "order-service-group";

    @KafkaListener(topics = ORDER_TOPIC, groupId = ORDER_SERVICE_GROUP)
    public void consumeMessage(String message) {

        logger.info("Pedido recebido com sucesso: {}", message);

    }
}
