package com.companyshield.demo.persistence.impl;

import com.companyshield.demo.domain.TransactionEntity;
import com.companyshield.demo.domain.TransactionType;
import com.companyshield.demo.persistence.TransactionRepository;
import com.companyshield.demo.util.RingBuffer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

@Service
public class InMemoryTransactionRepository implements TransactionRepository {

    private static final ConcurrentHashMap<String, InMemoryTransactionObject> nonOutgoingTransactions = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, InMemoryTransactionObject> outgoingTransactions = new ConcurrentHashMap<>();

    @Override
    public Optional<List<TransactionEntity>> findAllByUserId(String id) {
        if (!nonOutgoingTransactions.containsKey(id) || !outgoingTransactions.containsKey(id)) {
            return Optional.empty();
        }

        try {
            nonOutgoingTransactions.get(id).lock().readLock().lock();
            outgoingTransactions.get(id).lock().readLock().lock();

            return Optional.of(Stream.concat(
                    nonOutgoingTransactions.get(id).log().getBuffer().stream(),
                    outgoingTransactions.get(id).log().getBuffer().stream()
            ).toList());
        } finally {
            nonOutgoingTransactions.get(id).lock().readLock().unlock();
            outgoingTransactions.get(id).lock().readLock().unlock();
        }
    }

    @Override
    public boolean createHistoryForUser(String userId) {
        return nonOutgoingTransactions.putIfAbsent(userId, new InMemoryTransactionObject(new RingBuffer<>(50))) == null &&
                outgoingTransactions.putIfAbsent(userId, new InMemoryTransactionObject(new RingBuffer<>(50))) == null;
    }

    @Override
    public boolean save(String userId, TransactionEntity transactionEntity) {
        if (!nonOutgoingTransactions.containsKey(userId) || !outgoingTransactions.containsKey(userId)) {
            return false;
        }

        try {
            nonOutgoingTransactions.get(userId).lock().writeLock().lock();
            outgoingTransactions.get(userId).lock().writeLock().lock();

            if (transactionEntity.getType() == TransactionType.TRANSFER_OUTGOING) {
                outgoingTransactions.get(userId).log().add(transactionEntity);
            } else {
                nonOutgoingTransactions.get(userId).log().add(transactionEntity);
            }

            return true;
        } finally {
            nonOutgoingTransactions.get(userId).lock().writeLock().unlock();
            outgoingTransactions.get(userId).lock().writeLock().unlock();
        }
    }
}
