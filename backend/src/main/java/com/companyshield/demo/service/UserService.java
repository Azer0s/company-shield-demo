package com.companyshield.demo.service;


import com.companyshield.demo.domain.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    Optional<UserDto> findByUsername(String username);
    List<UserDto> findAll();
}