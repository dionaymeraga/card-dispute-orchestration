package io.github.dionaymeraga.disputeworkers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class TriageWorker {

    private final RestClient cardSystem = RestClient.create("http://localhost:8080");

    @JobWorker(type = "triage-dispute")
    public Map<String, Object> triage(@Variable(name = "transactionId") String transactionId) {
        if (transactionId == null) {
            throw new IllegalStateException("Process variable 'transactionId' is missing");
        }

        Map<?, ?> transaction = cardSystem.get()
                .uri("/transactions/{id}", transactionId)
                .retrieve()
                .body(Map.class);

        return Map.of(
                "transactionMatches", true,
                "category", "DUPLICATE_CHARGE",
                "confidence", "HIGH",
                "amount", transaction.get("amount"));
    }
}