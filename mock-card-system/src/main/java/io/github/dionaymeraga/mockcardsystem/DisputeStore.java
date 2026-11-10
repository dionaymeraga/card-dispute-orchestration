package io.github.dionaymeraga.mockcardsystem;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DisputeStore {

    private final List<Dispute> disputes = List.of(
            new Dispute("d-001", "card-04", "Item not received", "REFUNDED", LocalDate.parse("2026-09-05")),
            new Dispute("d-002", "card-04", "Item not as described", "REFUNDED", LocalDate.parse("2026-09-19")),
            new Dispute("d-003", "card-04", "Item not received", "REJECTED", LocalDate.parse("2026-10-02")),
            new Dispute("d-004", "card-04", "Unauthorised payment", "REFUNDED", LocalDate.parse("2026-10-14")),
            new Dispute("d-005", "card-04", "Item not received", "REFUNDED", LocalDate.parse("2026-10-28")),
            new Dispute("d-006", "card-01", "Wrong amount", "REFUNDED", LocalDate.parse("2025-03-11"))
    );

    public List<Dispute> findByCardId(String cardId) {
        List<Dispute> result = new ArrayList<>();
        for (Dispute dispute : disputes) {
            if (dispute.cardId().equals(cardId)) {
                result.add(dispute);
            }
        }
        return result;
    }
}