package io.github.dionaymeraga.mockscheme;

import java.math.BigDecimal;

public record ChargebackRequest(
        String pan,
        String acquirerReference,
        BigDecimal amount,
        String currency,
        String reasonCategory
) {}