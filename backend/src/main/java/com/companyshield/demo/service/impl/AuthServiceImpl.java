package com.companyshield.demo.service.impl;

import com.companyshield.demo.domain.transactional.LoginUserRequest;
import com.companyshield.demo.domain.transactional.LoginUserResponse;
import com.companyshield.demo.exception.UserNotFoundException;
import com.companyshield.demo.mapper.UserMapper;
import com.companyshield.demo.persistence.UserRepository;
import com.companyshield.demo.service.AuthService;
import com.companyshield.demo.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public LoginUserResponse login(LoginUserRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserNotFoundException(request.getUsername()));

        var token = jwtService.generateToken(user);

        return LoginUserResponse.builder()
                .token(token)
                .forUser(userMapper.toDto(user))
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }
}
