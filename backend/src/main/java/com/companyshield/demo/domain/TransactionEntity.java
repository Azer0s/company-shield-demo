package com.companyshield.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionEntity {
    private String fromId;
    private String toId;
    private String amount;
    private TransactionState state;
}
