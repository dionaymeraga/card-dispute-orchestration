package io.github.dionaymeraga.mockcardsystem;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MerchantStore {

    private final Map<String, Merchant> merchants = Map.of(
            "m-coffee", new Merchant("m-coffee", "Corner Coffee", "Food and drink", 0.2),
            "m-electro", new Merchant("m-electro", "QuickElectro Online", "Electronics", 7.5),
            "m-books", new Merchant("m-books", "City Books", "Books", 0.4),
            "m-fashion", new Merchant("m-fashion", "Urban Style", "Clothing", 1.1)
    );

    public Merchant findById(String id) {
        return merchants.get(id);
    }
}