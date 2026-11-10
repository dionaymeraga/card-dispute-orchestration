package io.github.dionaymeraga.mockcardsystem;

public record Merchant(
        String id,
        String name,
        String category,
        double disputeRatePercent
) {}