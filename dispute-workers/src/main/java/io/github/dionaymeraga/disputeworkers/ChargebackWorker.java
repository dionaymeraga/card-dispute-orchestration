package io.github.dionaymeraga.disputeworkers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.Map;

@Component
public class ChargebackWorker {

    private final RestClient cardSystem = RestClient.create("http://localhost:8080");
    private final RestClient scheme = RestClient.create("http://localhost:8082");

    @JobWorker(type = "initiate-chargeback")
    public Map<String, Object> initiateChargeback(
            @Variable(name = "transactionId") String transactionId,
            @Variable(name = "category") String category) {

        if (transactionId == null) {
            throw new IllegalStateException("Process variable 'transactionId' is missing");
        }

        Map<?, ?> transaction = cardSystem.get()
                .uri("/transactions/{id}", transactionId)
                .retrieve()
                .body(Map.class);

        Map<?, ?> card = cardSystem.get()
                .uri("/cards/{cardId}/pan", transaction.get("cardId"))
                .retrieve()
                .body(Map.class);

        Map<String, Object> filing = new HashMap<>();
        filing.put("pan", card.get("pan"));
        filing.put("acquirerReference", transaction.get("acquirerReference"));
        filing.put("amount", transaction.get("amount"));
        filing.put("currency", transaction.get("currency"));
        filing.put("reasonCategory", category);

        try {
            Map<?, ?> chargeback = scheme.post()
                    .uri("/chargebacks")
                    .body(filing)
                    .retrieve()
                    .body(Map.class);

            return Map.of(
                    "chargebackCaseId", chargeback.get("caseId"),
                    "chargebackStatus", "FILED");

        } catch (HttpClientErrorException.Conflict alreadyFiled) {
            return Map.of("chargebackStatus", "ALREADY_FILED");
        }
    }
}