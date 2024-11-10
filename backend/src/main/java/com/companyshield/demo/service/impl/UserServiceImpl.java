package com.companyshield.demo.service.impl;

import com.companyshield.demo.domain.UserEntity;
import com.companyshield.demo.domain.dto.UserDto;
import com.companyshield.demo.domain.transactional.CreateUserRequest;
import com.companyshield.demo.mapper.UserMapper;
import com.companyshield.demo.persistence.TransactionRepository;
import com.companyshield.demo.persistence.UserRepository;
import com.companyshield.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean createUser(CreateUserRequest createUserRequest) {
        var user = UserEntity.builder()
                .username(createUserRequest.getUsername())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .roles(createUserRequest.getRoles())
                .balance(BigDecimal.ZERO)
                .disabled(false)
                .build();

        if (!userRepository.save(user)) {
            return false;
        }

        if (!transactionRepository.createHistoryForUser(user.getId())) {
            userRepository.deleteById(user.getId());
            return false;
        }

        return true;
    }

    @Override
    public Optional<UserDto> getUserById(String id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}