package com.companyshield.demo.service;

import com.companyshield.demo.domain.transactional.LoginUserRequest;
import com.companyshield.demo.domain.transactional.LoginUserResponse;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    LoginUserResponse login(LoginUserRequest request);
}
