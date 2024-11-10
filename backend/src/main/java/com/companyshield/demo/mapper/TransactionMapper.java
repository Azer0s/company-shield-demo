package com.companyshield.demo.mapper;

import com.companyshield.demo.domain.TransactionEntity;
import com.companyshield.demo.domain.dto.TransactionDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface TransactionMapper {
    TransactionDto toDto(TransactionEntity transactionEntity);
}