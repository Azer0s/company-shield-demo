package com.companyshield.demo.controller;

import com.companyshield.demo.domain.TransactionType;
import com.companyshield.demo.domain.UserEntity;
import com.companyshield.demo.domain.transactional.DepositRequest;
import com.companyshield.demo.domain.transactional.GetTransactionsResponse;
import com.companyshield.demo.domain.transactional.TransferRequest;
import com.companyshield.demo.service.TransactionService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@Controller
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/me")
    public @ResponseBody GetTransactionsResponse getTransactions() {
        var currentUser = (UserEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return new GetTransactionsResponse(transactionService.getTransactionsForUser(currentUser.getId()));
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody boolean deposit(@RequestBody DepositRequest request) {
        return transactionService.deposit(request.getUserId(), request.getAmount());
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody boolean withdraw(@RequestBody DepositRequest request) {
        return transactionService.withdraw(request.getUserId(), request.getAmount());
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('USER')")
    public @ResponseBody boolean transfer(@RequestBody TransferRequest request) {
        var currentUser = (UserEntity) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        return transactionService.transfer(currentUser.getId(), request.getToUserId(), request.getAmount());
    }
}
