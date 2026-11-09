package io.github.dionaymeraga.mockcardsystem;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Component
public class TransactionStore {

    private final Map<String, Transaction> transactions = Map.of(
            "tx-1001", new Transaction("tx-1001", "card-01", "m-coffee",
                    new BigDecimal("4.50"), "EUR", Instant.parse("2026-11-02T08:15:00Z")),
            "tx-1002", new Transaction("tx-1002", "card-01", "m-coffee",
                    new BigDecimal("4.50"), "EUR", Instant.parse("2026-11-02T08:17:00Z"))
    );

    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactions.get(id));
    }
}