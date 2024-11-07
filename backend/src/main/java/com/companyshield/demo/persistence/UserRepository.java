package com.companyshield.demo.persistence;

import com.companyshield.demo.domain.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserRepository {
    boolean create(UserEntity user);
    Optional<UserEntity> findById(String id);
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findAll();
}