package com.companyshield.demo.service;


import com.companyshield.demo.domain.dto.UserDto;
import com.companyshield.demo.domain.transactional.CreateUserRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    boolean createUser(CreateUserRequest createUserRequest);
    Optional<UserDto> getUserById(String id);
    List<UserDto> getAllUsers();
}