package io.github.dionaymeraga.disputeworkers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
public class TriageAgent {

    private final RestClient claude;

    public TriageAgent(@Value("${anthropic.api-key}") String apiKey) {
        this.claude = RestClient.builder()
                .baseUrl("https://api.anthropic.com")
                .defaultHeader("x-api-key", apiKey)
                .defaultHeader("anthropic-version", "2023-06-01")
                .build();
    }

    public String ask(String prompt) {
        Map<String, Object> request = Map.of(
                "model", "claude-haiku-5-5",
                "max_tokens", 500,
                "messages", List.of(Map.of("role", "user", "content", prompt)));

        Map<?, ?> response = claude.post()
                .uri("/v1/messages")
                .body(request)
                .retrieve()
                .body(Map.class);

        List<?> content = (List<?>) response.get("content");
        for (Object block : content) {
            Map<?, ?> b = (Map<?, ?>) block;
            if ("text".equals(b.get("type"))) {
                return (String) b.get("text");
            }
        }
        throw new IllegalStateException("No text block in response: " + response);
    }
}