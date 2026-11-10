package io.github.dionaymeraga.mockcardsystem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DisputeController {

    private final DisputeStore store;

    public DisputeController(DisputeStore store) {
        this.store = store;
    }

    @GetMapping("/cards/{cardId}/disputes")
    public List<Dispute> getDisputesForCard(@PathVariable String cardId) {
        return store.findByCardId(cardId);
    }
}