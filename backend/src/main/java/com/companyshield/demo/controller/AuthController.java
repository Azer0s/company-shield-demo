package com.companyshield.demo.controller;

import com.companyshield.demo.domain.transactional.LoginUserRequest;
import com.companyshield.demo.domain.transactional.LoginUserResponse;
import com.companyshield.demo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody LoginUserRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/verify")
    public ResponseEntity verify() {
        return ResponseEntity.ok().build();
    }
}
