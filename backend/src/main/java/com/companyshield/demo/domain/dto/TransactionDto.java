package com.companyshield.demo.domain.dto;

import com.companyshield.demo.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class TransactionDto {
    private Long timestamp;
    private String fromId;
    private String toId;
    private BigDecimal accountBalanceBefore;
    private BigDecimal accountBalanceAfter;
    private TransactionType type;
}
