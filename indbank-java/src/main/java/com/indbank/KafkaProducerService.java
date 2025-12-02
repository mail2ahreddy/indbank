package com.indbank;

import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // Publish real JSON
    public void publishTransactionEvent(String topic, Long accountId, String type, double amount) {
        JSONObject event = new JSONObject();
        event.put("account", accountId.toString());
        event.put("type", type);
        event.put("amount", amount);

        kafkaTemplate.send(topic, event.toString());
    }
}
