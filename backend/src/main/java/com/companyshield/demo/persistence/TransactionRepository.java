package com.companyshield.demo.persistence;

import com.companyshield.demo.domain.TransactionEntity;
import com.companyshield.demo.domain.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;

@Service
public interface TransactionRepository {
    Optional<List<TransactionEntity>> findAllByUserId(String id);
    boolean createHistoryForUser(String userId);
    boolean save(String userId, TransactionEntity transactionEntity);
}
