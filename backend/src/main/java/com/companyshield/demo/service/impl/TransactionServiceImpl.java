package com.companyshield.demo.service.impl;

import com.companyshield.demo.domain.TransactionEntity;
import com.companyshield.demo.domain.TransactionType;
import com.companyshield.demo.domain.dto.TransactionDto;
import com.companyshield.demo.mapper.TransactionMapper;
import com.companyshield.demo.persistence.TransactionRepository;
import com.companyshield.demo.persistence.UserRepository;
import com.companyshield.demo.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public List<TransactionDto> getTransactionsForUser(String userId) {
        return transactionRepository.findAllByUserId(userId)
                .map(l -> l.stream()
                        .map(transactionMapper::toDto)
                        .sorted(Comparator.comparing(TransactionDto::getTimestamp).reversed())
                        .toList())
                .orElseThrow();
    }

    @Override
    public boolean deposit(String userId, BigDecimal amount) {
        var user = userRepository.findById(userId).orElseThrow();

        var res = userRepository.withLock(List.of(userId), () -> {
            var accountBalanceBefore = user.getBalance();
            user.setBalance(user.getBalance().add(amount));

            var accountBalanceAfter = user.getBalance();

            if (!userRepository.update(user)) {
                return Optional.of(new IllegalStateException("Failed to update user balance"));
            }

            if (!transactionRepository.save(userId, TransactionEntity.builder()
                    .timestamp(System.currentTimeMillis())
                    .fromId(null)
                    .toId(userId)
                    .accountBalanceBefore(accountBalanceBefore)
                    .accountBalanceAfter(accountBalanceAfter)
                    .type(TransactionType.DEPOSIT)
                    .build())) {
                user.setBalance(accountBalanceBefore);
                userRepository.update(user);

                return Optional.of(new IllegalStateException("Failed to save transaction"));
            }

            return Optional.empty();
        });

        return res.isEmpty();
    }

    @Override
    public boolean withdraw(String userId, BigDecimal amount) {
        var user = userRepository.findById(userId).orElseThrow();

        var res = userRepository.withLock(List.of(userId), () -> {
            var accountBalanceBefore = user.getBalance();
            user.setBalance(user.getBalance().subtract(amount));

            var accountBalanceAfter = user.getBalance();

            if (user.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                return Optional.of(new IllegalStateException("Insufficient funds"));
            }

            if (!userRepository.update(user)) {
                return Optional.of(new IllegalStateException("Failed to update user balance"));
            }

            if (!transactionRepository.save(userId, TransactionEntity.builder()
                    .timestamp(System.currentTimeMillis())
                    .fromId(userId)
                    .toId(null)
                    .accountBalanceBefore(accountBalanceBefore)
                    .accountBalanceAfter(accountBalanceAfter)
                    .type(TransactionType.WITHDRAWAL)
                    .build())) {
                user.setBalance(accountBalanceBefore);
                userRepository.update(user);

                return Optional.of(new IllegalStateException("Failed to save transaction"));
            }

            return Optional.empty();
        });

        return res.isEmpty();
    }

    @Override
    public boolean transfer(String fromUserId, String toUserId, BigDecimal amount) {
        var fromUser = userRepository.findById(fromUserId).orElseThrow();
        var toUser = userRepository.findById(toUserId).orElseThrow();

        var res = userRepository.withLock(List.of(fromUserId, toUserId), () -> {
            var fromAccountBalanceBefore = fromUser.getBalance();
            var toAccountBalanceBefore = toUser.getBalance();

            fromUser.setBalance(fromUser.getBalance().subtract(amount));
            toUser.setBalance(toUser.getBalance().add(amount));

            var fromAccountBalanceAfter = fromUser.getBalance();
            var toAccountBalanceAfter = toUser.getBalance();

            if (fromUser.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                return Optional.of(new IllegalStateException("Insufficient funds"));
            }

            if (!userRepository.update(fromUser)) {
                return Optional.of(new IllegalStateException("Failed to update user balance"));
            }

            if (!userRepository.update(toUser)) {
                fromUser.setBalance(fromAccountBalanceBefore);
                userRepository.update(fromUser);

                return Optional.of(new IllegalStateException("Failed to update user balance"));
            }

            if (!transactionRepository.save(fromUserId, TransactionEntity.builder()
                    .timestamp(System.currentTimeMillis())
                    .fromId(fromUserId)
                    .toId(toUserId)
                    .accountBalanceBefore(fromAccountBalanceBefore)
                    .accountBalanceAfter(fromAccountBalanceAfter)
                    .type(TransactionType.TRANSFER_OUTGOING)
                    .build())) {
                fromUser.setBalance(fromAccountBalanceBefore);
                userRepository.update(fromUser);
                userRepository.update(toUser);

                return Optional.of(new IllegalStateException("Failed to save transaction"));
            }

            if (!transactionRepository.save(toUserId, TransactionEntity.builder()
                    .timestamp(System.currentTimeMillis())
                    .fromId(fromUserId)
                    .toId(toUserId)
                    .accountBalanceBefore(toAccountBalanceBefore)
                    .accountBalanceAfter(toAccountBalanceAfter)
                    .type(TransactionType.TRANSFER_INCOMING)
                    .build())) {
                fromUser.setBalance(fromAccountBalanceBefore);
                userRepository.update(fromUser);
                userRepository.update(toUser);

                return Optional.of(new IllegalStateException("Failed to save transaction"));
            }

            return Optional.empty();
        });

        return res.isEmpty();
    }
}
