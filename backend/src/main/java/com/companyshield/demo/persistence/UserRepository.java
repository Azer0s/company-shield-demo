package com.companyshield.demo.persistence;

import com.companyshield.demo.domain.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
public interface UserRepository {
    boolean save(UserEntity user);
    boolean update(UserEntity user);
    void deleteById(String id);
    Optional<UserEntity> findById(String id);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findAll();

    Optional<Exception> withLock(List<String> toLock, Callable<Optional<Exception>> callback);
}