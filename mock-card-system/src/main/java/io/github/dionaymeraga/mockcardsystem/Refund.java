package io.github.dionaymeraga.mockcardsystem;

import java.math.BigDecimal;
import java.time.Instant;

public record Refund(
        String id,
        String transactionId,
        BigDecimal amount,
        String currency,
        String reason,
        Instant createdAt
) {}