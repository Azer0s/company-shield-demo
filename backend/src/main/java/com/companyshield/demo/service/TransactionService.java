package com.companyshield.demo.service;

import com.companyshield.demo.domain.dto.TransactionDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface TransactionService {
    List<TransactionDto> getTransactionsForUser(String userId);
    boolean deposit(String userId, BigDecimal amount);
    boolean withdraw(String userId, BigDecimal amount);
    boolean transfer(String fromUserId, String toUserId, BigDecimal amount);
}
