package com.companyshield.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransactionEntity {
    private Long timestamp;
    private String fromId;
    private String toId;
    private BigDecimal accountBalanceBefore;
    private BigDecimal accountBalanceAfter;
    private TransactionType type;
}
