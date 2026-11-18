package io.github.dionaymeraga.disputeworkers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotifyBankWorker {

    @JobWorker(type = "notify-bank")
    public Map<String, Object> notifyBank(
            @Variable(name = "transactionId") String transactionId,
            @Variable(name = "analystNote") String analystNote) {

        System.out.println("Notifying bank: no chargeback for " + transactionId
                + ". Reason: " + analystNote);

        return Map.of("bankNotified", true);
    }
}