package io.github.dionaymeraga.mockcardsystem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class RefundController {

    private final TransactionStore transactionStore;
    private final Map<String, Refund> refundsByTransactionId = new HashMap<>();

    public RefundController(TransactionStore transactionStore) {
        this.transactionStore = transactionStore;
    }

    @PostMapping("/refunds")
    public ResponseEntity<Refund> createRefund(@RequestBody RefundRequest request) {
        String transactionId = request.transactionId();

        if ("tx-5001".equals(transactionId)) {
            return ResponseEntity.status(503).build();
        }

        Optional<Transaction> found = transactionStore.findById(transactionId);
        if (found.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (refundsByTransactionId.containsKey(transactionId)) {
            return ResponseEntity.status(409).build();
        }

        Transaction transaction = found.get();
        String refundId = "rf-" + (refundsByTransactionId.size() + 1);
        Refund refund = new Refund(refundId, transactionId, transaction.amount(),
                transaction.currency(), request.reason(), Instant.now());
        refundsByTransactionId.put(transactionId, refund);

        return ResponseEntity.status(201).body(refund);
    }
}