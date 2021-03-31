package com.example.demo.accountms.application.internal.commandgateways;

import java.util.concurrent.CompletableFuture;

import com.example.demo.accountms.domain.commands.CreateAccountCommand;
import com.example.demo.accountms.domain.commands.DepositMoneyCommand;
import com.example.demo.accountms.domain.commands.WithdrawMoneyCommand;
import com.example.demo.accountms.domain.model.BankAccountAggregate;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountCommandService {

    private final CommandGateway commandGateway;

    public CompletableFuture<String> createAccount(CreateAccountCommand createAccountCommand) {
        log.debug("createAccount command={}", createAccountCommand);
        return commandGateway.send(createAccountCommand);
    }

    public CompletableFuture<String> depositMoney(DepositMoneyCommand depositMoneyCommand) {
        log.debug("depositMoney command={}", depositMoneyCommand);
        return commandGateway.send(depositMoneyCommand);
    }

    public CompletableFuture<String> withdrawMoney(WithdrawMoneyCommand withdrawMoneyCommand) {
        log.debug("withdrawMoney command={}", withdrawMoneyCommand);
        return commandGateway.send(withdrawMoneyCommand);
    }

}