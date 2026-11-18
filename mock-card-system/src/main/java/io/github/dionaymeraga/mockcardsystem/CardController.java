package io.github.dionaymeraga.mockcardsystem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class CardController {

    private final Map<String, String> pansByCardId = Map.of(
            "card-01", "5100000000000001",
            "card-02", "5100000000000002",
            "card-03", "5100000000000003",
            "card-04", "5100000000000004",
            "card-05", "5100000000000005"
    );

    @GetMapping("/cards/{cardId}/pan")
    public ResponseEntity<CardPan> getPan(@PathVariable String cardId) {
        String pan = pansByCardId.get(cardId);
        if (pan == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new CardPan(cardId, pan));
    }
}