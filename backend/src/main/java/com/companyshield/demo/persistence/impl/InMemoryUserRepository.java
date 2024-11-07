package com.companyshield.demo.persistence.impl;

import com.companyshield.demo.domain.UserEntity;
import com.companyshield.demo.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import static java.util.Optional.empty;

@Service
public class InMemoryUserRepository implements UserRepository {
    private static final ConcurrentHashMap<String, InMemoryUserObject> users = new ConcurrentHashMap<>();

    @Override
    public boolean create(UserEntity user) {
        var userObject = new InMemoryUserObject(user);
        return users.putIfAbsent(user.getId(), userObject) == null;
    }

    @Override
    public Optional<UserEntity> findById(String id) {
        var user = new AtomicReference<Optional<UserEntity>>(empty());
        users.computeIfPresent(id, (_, value) -> {
            value.lock().readLock().lock();
            try {
                user.set(Optional.of(value.userEntity()));
                return value;
            } finally {
                value.lock().readLock().unlock();
            }
        });

        return user.get();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        var user = new AtomicReference<Optional<UserEntity>>(empty());
        users.values().forEach(userObject -> {
            userObject.lock().readLock().lock();
            try {
                if (userObject.userEntity().getUsername().equals(username)) {
                    user.set(Optional.of(userObject.userEntity()));
                }
            } finally {
                userObject.lock().readLock().unlock();
            }
        });

        return user.get();
    }

    @Override
    public List<UserEntity> findAll() {
        return users.values().stream()
                .map(InMemoryUserObject::userEntity)
                .toList();
    }
}
