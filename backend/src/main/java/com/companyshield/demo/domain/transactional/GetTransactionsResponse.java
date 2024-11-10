package com.companyshield.demo.domain.transactional;

import com.companyshield.demo.domain.dto.TransactionDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GetTransactionsResponse {
    private List<TransactionDto> transactions;
}
