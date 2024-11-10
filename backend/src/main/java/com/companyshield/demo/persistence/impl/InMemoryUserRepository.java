package com.companyshield.demo.persistence.impl;

import com.companyshield.demo.domain.UserEntity;
import com.companyshield.demo.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Optional.empty;

@Service
public class InMemoryUserRepository implements UserRepository {
    private static final ConcurrentHashMap<String, InMemoryUserObject> users = new ConcurrentHashMap<>();

    @Override
    public boolean save(UserEntity user) {
        var userObject = new InMemoryUserObject(user);
        return users.putIfAbsent(user.getId(), userObject) == null;
    }

    @Override
    public boolean update(UserEntity user) {
        if (users.containsKey(user.getId())) {
            var lock = users.get(user.getId()).lock();
            users.put(user.getId(), new InMemoryUserObject(user, lock));
            return true;
        }
        return false;
    }

    @Override
    public void deleteById(String id) {
        users.remove(id);
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

    @Override
    public Optional<Exception> withLock(List<String> toLock, Callable<Optional<Exception>> callback) {
        var areAllPresent = toLock.stream()
                .allMatch(users::containsKey);

        if (!areAllPresent) {
            return Optional.of(new IllegalArgumentException("Not all users exist"));
        }

        try {
            toLock.forEach(id -> users.get(id).lock().writeLock().lock());
            return callback.call();
        } catch (Exception e) {
            return Optional.of(e);
        } finally {
            toLock.forEach(id -> users.get(id).lock().writeLock().unlock());
        }
    }
}
