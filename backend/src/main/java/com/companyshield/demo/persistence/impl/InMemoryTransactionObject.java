package com.companyshield.demo.persistence.impl;

import com.companyshield.demo.domain.TransactionEntity;
import com.companyshield.demo.util.RingBuffer;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public record InMemoryTransactionObject(RingBuffer<TransactionEntity> log, ReadWriteLock lock) {
    public InMemoryTransactionObject(RingBuffer<TransactionEntity> log) {
        this(log, new ReentrantReadWriteLock());
    }
}
