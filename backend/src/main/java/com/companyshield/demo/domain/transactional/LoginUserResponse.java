package com.companyshield.demo.domain.transactional;


import com.companyshield.demo.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class LoginUserResponse {
    private String token;
    private long expiresIn;
    private UserDto forUser;
}
