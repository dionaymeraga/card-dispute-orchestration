package io.github.dionaymeraga.mockscheme;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ChargebackController {

    private final Map<String, Chargeback> casesByReference = new HashMap<>();

    @PostMapping("/chargebacks")
    public ResponseEntity<Chargeback> fileChargeback(@RequestBody ChargebackRequest request) {

        if (request.pan() == null || request.acquirerReference() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (casesByReference.containsKey(request.acquirerReference())) {
            return ResponseEntity.status(409).build();
        }

        String caseId = "CB-" + (casesByReference.size() + 1);
        Chargeback chargeback = new Chargeback(
                caseId, request.acquirerReference(), "FILED", Instant.now());
        casesByReference.put(request.acquirerReference(), chargeback);

        System.out.println("Chargeback " + caseId + " filed for "
                + request.acquirerReference() + ", PAN ending "
                + request.pan().substring(request.pan().length() - 4));

        return ResponseEntity.status(201).body(chargeback);
    }
}