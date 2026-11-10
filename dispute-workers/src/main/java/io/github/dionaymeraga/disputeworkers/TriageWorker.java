package io.github.dionaymeraga.disputeworkers;

import io.camunda.client.annotation.JobWorker;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TriageWorker {

    @JobWorker(type = "triage-dispute")
    public Map<String, Object> triage() {
        return Map.of("recommendedOutcome", "REFUND");
    }
}