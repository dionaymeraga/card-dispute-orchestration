package io.github.dionaymeraga.mockcardsystem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MerchantController {

    private final MerchantStore store;

    public MerchantController(MerchantStore store) {
        this.store = store;
    }

    @GetMapping("/merchants/{id}")
    public ResponseEntity<Merchant> getMerchant(@PathVariable String id) {
        Merchant merchant = store.findById(id);
        if (merchant == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(merchant);
    }
}