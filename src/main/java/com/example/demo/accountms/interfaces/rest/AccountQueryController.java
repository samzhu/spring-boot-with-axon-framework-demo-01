package com.example.demo.accountms.interfaces.rest;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.demo.accountms.application.internal.querygateways.BankAccountQueryService;
import com.example.demo.accountms.interfaces.rest.dto.BankAccountDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/accounts")
@Api(value = "Bank Account Queries", description = "Bank Account Query Events API")
@RequiredArgsConstructor
public class AccountQueryController {
    private final BankAccountQueryService bankAccountQueryService;

    @GetMapping("/{accountNumber}")
    public CompletableFuture<BankAccountDto> findById(@PathVariable("accountNumber") String accountNumber) {
        return this.bankAccountQueryService.findById(accountNumber);
    }

    @GetMapping("/{accountNumber}/events")
    public List<Object> listEventsForAccount(@PathVariable(value = "accountNumber") String accountNumber) {
        return this.bankAccountQueryService.listEventsForAccount(accountNumber);
    }
}