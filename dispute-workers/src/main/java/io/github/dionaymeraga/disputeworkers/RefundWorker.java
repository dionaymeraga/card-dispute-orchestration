package io.github.dionaymeraga.disputeworkers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class RefundWorker {

    private final RestClient cardSystem = RestClient.create("http://localhost:8080");

    @JobWorker(type = "issue-refund")
    public Map<String, Object> issueRefund(@Variable(name = "transactionId") String transactionId) {
        if (transactionId == null) {
            throw new IllegalStateException("Process variable 'transactionId' is missing");
        }

        Map<String, String> request = Map.of(
                "transactionId", transactionId,
                "reason", "Dispute approved");

        try {
            Map<?, ?> refund = cardSystem.post()
                    .uri("/refunds")
                    .body(request)
                    .retrieve()
                    .body(Map.class);
            return Map.of("refundId", refund.get("id"), "refundStatus", "REFUNDED");
        } catch (HttpClientErrorException.Conflict alreadyRefunded) {
            return Map.of("refundStatus", "ALREADY_REFUNDED");
        }
    }
}