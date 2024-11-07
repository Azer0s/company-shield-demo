package com.companyshield.demo.mapper;


import com.companyshield.demo.domain.UserEntity;
import com.companyshield.demo.domain.dto.UserDto;
import com.companyshield.demo.domain.transactional.GetUserResponse;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserDto toDto(UserEntity userEntity);
}