package io.github.dionaymeraga.mockcardsystem;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionStore {

    private final Map<String, Transaction> transactions = Map.of(
            "tx-1001", new Transaction("tx-1001", "card-01", "m-coffee",
                    new BigDecimal("4.50"), "EUR", Instant.parse("2026-11-02T08:15:00Z")),
            "tx-1002", new Transaction("tx-1002", "card-01", "m-coffee",
                    new BigDecimal("4.50"), "EUR", Instant.parse("2026-11-02T08:17:00Z")),
            "tx-2001", new Transaction("tx-2001", "card-02", "m-electro",
                    new BigDecimal("1899.00"), "EUR", Instant.parse("2026-11-03T23:41:00Z")),
            "tx-3001", new Transaction("tx-3001", "card-03", "m-books",
                    new BigDecimal("42.90"), "EUR", Instant.parse("2026-10-20T14:05:00Z")),
            "tx-4001", new Transaction("tx-4001", "card-04", "m-fashion",
                    new BigDecimal("129.99"), "EUR", Instant.parse("2026-11-01T17:30:00Z")),
            "tx-5001", new Transaction("tx-5001", "card-05", "m-coffee",
                    new BigDecimal("3.20"), "EUR", Instant.parse("2026-11-05T09:10:00Z"))
    );

    public Optional<Transaction> findById(String id) {
        return Optional.ofNullable(transactions.get(id));
    }

    public List<Transaction> findByCardId(String cardId) {
        List<Transaction> result = new ArrayList<>();
        for (Transaction transaction : transactions.values()) {
            if (transaction.cardId().equals(cardId)) {
                result.add(transaction);
            }
        }
        return result;
    }
}