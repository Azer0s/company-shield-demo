package com.companyshield.demo.domain.transactional;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class TransferRequest {
    private String toUserId;
    private BigDecimal amount;
}
