package com.companyshield.demo.domain.transactional;

import com.companyshield.demo.domain.UserRole;
import com.companyshield.demo.domain.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetUserResponse {
    private UserDto user;
}
