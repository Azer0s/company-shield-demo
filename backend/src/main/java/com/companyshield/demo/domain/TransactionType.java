package com.companyshield.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionType {
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL"),
    TRANSFER_OUTGOING("TRANSFER_OUTGOING"),
    TRANSFER_INCOMING("TRANSFER_INCOMING");

    private final String type;
}
