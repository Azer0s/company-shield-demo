package com.companyshield.demo.domain.transactional;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class WithdrawalRequest {
    private BigDecimal amount;
    private String userId;
}
