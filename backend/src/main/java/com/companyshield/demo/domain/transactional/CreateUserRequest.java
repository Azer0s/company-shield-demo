package com.companyshield.demo.domain.transactional;

import com.companyshield.demo.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequest {
    private String username;
    private String password;
    private List<UserRole> roles;
}
