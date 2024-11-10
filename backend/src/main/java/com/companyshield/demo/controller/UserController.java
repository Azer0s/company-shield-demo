package com.companyshield.demo.controller;

import com.companyshield.demo.domain.UserEntity;
import com.companyshield.demo.domain.transactional.CreateUserRequest;
import com.companyshield.demo.domain.transactional.GetAllUsersResponse;
import com.companyshield.demo.domain.transactional.GetUserResponse;
import com.companyshield.demo.mapper.UserMapper;
import com.companyshield.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/api/user")
public class UserController {
    private UserService userService;
    private UserMapper userMapper;

    @GetMapping("/")
    public @ResponseBody GetAllUsersResponse getAllUsers() {
        return new GetAllUsersResponse(userService.getAllUsers());
    }

    @PostMapping("/")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody boolean createUser(@RequestBody CreateUserRequest createUserRequest) {
        return userService.createUser(createUserRequest);
    }

    @GetMapping("/me")
    public @ResponseBody GetUserResponse getUserById() {
        var currentUser = (UserEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return new GetUserResponse(userMapper.toDto(currentUser));
    }
}
