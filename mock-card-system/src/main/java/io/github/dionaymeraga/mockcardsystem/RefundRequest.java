package io.github.dionaymeraga.mockcardsystem;

public record RefundRequest(String transactionId, String reason) {}