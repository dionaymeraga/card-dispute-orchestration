package io.github.dionaymeraga.mockcardsystem;

import java.time.LocalDate;

public record Dispute(
        String id,
        String cardId,
        String reason,
        String outcome,
        LocalDate openedOn
) {}