package com.companyshield.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionState {
    SUCCESS("SUCCESS"),
    FAILED("FAILED");

    private final String state;
}
