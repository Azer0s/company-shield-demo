package com.companyshield.demo.persistence.impl;

import com.companyshield.demo.domain.UserEntity;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public record InMemoryUserObject(UserEntity userEntity, ReadWriteLock lock) {
    public InMemoryUserObject(UserEntity userEntity) {
        this(userEntity, new ReentrantReadWriteLock());
    }
}
