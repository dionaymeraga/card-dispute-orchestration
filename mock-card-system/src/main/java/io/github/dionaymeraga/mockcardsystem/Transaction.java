package io.github.dionaymeraga.mockcardsystem;

import java.math.BigDecimal;
import java.time.Instant;

public record Transaction(
        String id,
        String acquirerReference,
        String cardId,
        String merchantId,
        BigDecimal amount,
        String currency,
        Instant timestamp
) {}