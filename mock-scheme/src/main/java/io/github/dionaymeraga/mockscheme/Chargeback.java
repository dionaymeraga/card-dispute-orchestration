package io.github.dionaymeraga.mockscheme;

import java.time.Instant;

public record Chargeback(
        String caseId,
        String acquirerReference,
        String status,
        Instant filedAt
) {}