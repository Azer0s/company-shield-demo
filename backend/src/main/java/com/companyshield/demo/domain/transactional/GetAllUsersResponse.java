package com.companyshield.demo.domain.transactional;

import com.companyshield.demo.domain.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetAllUsersResponse {
    private List<UserDto> users;
}
