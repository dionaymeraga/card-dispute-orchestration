package io.github.dionaymeraga.mockcardsystem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TransactionController {

    private final TransactionStore store;

    public TransactionController(TransactionStore store) {
        this.store = store;
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable String id) {
        return store.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cards/{cardId}/transactions")
    public List<Transaction> getTransactionsForCard(@PathVariable String cardId) {
        return store.findByCardId(cardId);
    }
}