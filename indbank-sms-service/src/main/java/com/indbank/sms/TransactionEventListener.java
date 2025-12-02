package com.indbank.sms;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

@Component
public class TransactionEventListener {

    private final SmsService smsService;

    public TransactionEventListener(SmsService smsService) {
        this.smsService = smsService;
    }

    @KafkaListener(topics = "${sms.topic}", groupId = "sms-service-group")
    public void listen(String message) {
        try {
            JSONObject obj = new JSONObject(message);
            String account = obj.optString("account", "unknown");
            String type = obj.optString("type", "unknown");
            double amount = obj.optDouble("amount", 0.0);

            smsService.save(account, type, amount);
            System.out.println("SMS Saved: " + message);
        } catch (Exception e) {
            System.err.println("Error parsing Kafka message: " + e.getMessage());
        }
    }
}
