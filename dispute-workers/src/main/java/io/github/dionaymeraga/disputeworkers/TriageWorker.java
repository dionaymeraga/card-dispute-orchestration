package io.github.dionaymeraga.disputeworkers;

import io.camunda.client.annotation.JobWorker;
import io.camunda.client.annotation.Variable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TriageWorker {

    private final RestClient cardSystem = RestClient.create("http://localhost:8080");
    private final TriageAgent agent;
    private final ObjectMapper json;

    public TriageWorker(TriageAgent agent, ObjectMapper json) {
        this.agent = agent;
        this.json = json;
    }

    @JobWorker(type = "triage-dispute")
    public Map<String, Object> triage(
            @Variable(name = "transactionId") String transactionId,
            @Variable(name = "cardholderStatement") String cardholderStatement) {

        if (transactionId == null) {
            throw new IllegalStateException("Process variable 'transactionId' is missing");
        }

        Map<?, ?> transaction = cardSystem.get()
                .uri("/transactions/{id}", transactionId)
                .retrieve()
                .body(Map.class);

        List<?> cardTransactions = cardSystem.get()
                .uri("/cards/{cardId}/transactions", transaction.get("cardId"))
                .retrieve()
                .body(List.class);

        String prompt = """
                You are triaging a card dispute.

                The cardholder says: "%s"

                The disputed transaction: %s

                All recent transactions on this card: %s

                Reply with only a JSON object, nothing else:
                {"transactionMatches": true or false,
                 "category": "DUPLICATE_CHARGE or FRAUD or ITEM_NOT_RECEIVED or WRONG_AMOUNT or OTHER",
                 "confidence": "HIGH or MEDIUM or LOW",
                 "reasoning": "one sentence"}
                """.formatted(cardholderStatement, transaction, cardTransactions);

        String answer = agent.ask(prompt);
        System.out.println("CLAUDE SAID: " + answer);

        Map<?, ?> triage = json.readValue(stripFences(answer), Map.class);

        Map<String, Object> result = new HashMap<>();
        result.put("transactionMatches", triage.get("transactionMatches"));
        result.put("category", triage.get("category"));
        result.put("confidence", triage.get("confidence"));
        result.put("agentReasoning", triage.get("reasoning"));
        result.put("amount", transaction.get("amount"));
        return result;
    }

    private String stripFences(String text) {
        String trimmed = text.trim();
        if (trimmed.startsWith("```")) {
            int firstNewline = trimmed.indexOf('\n');
            int lastFence = trimmed.lastIndexOf("```");
            return trimmed.substring(firstNewline + 1, lastFence).trim();
        }
        return trimmed;
    }
}