package com.example.demo.accountms.interfaces.rest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import com.example.demo.accountms.application.internal.commandgateways.BankAccountCommandService;
import com.example.demo.accountms.application.internal.querygateways.BankAccountQueryService;
import com.example.demo.accountms.domain.commands.CreateAccountCommand;
import com.example.demo.accountms.domain.commands.DepositMoneyCommand;
import com.example.demo.accountms.infrastructure.BankAccountRepository;
import com.example.demo.accountms.interfaces.rest.dto.BankAccountCreatedDTO;
import com.example.demo.accountms.interfaces.rest.dto.BankAccountCreationDTO;
import com.example.demo.accountms.interfaces.rest.dto.BankAccountDto;
import com.example.demo.accountms.interfaces.rest.dto.MoneyAmountDTO;
import com.example.demo.accountms.interfaces.rest.transform.BankAccountAssembler;

import org.axonframework.queryhandling.QueryGateway;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/accounts")
public class AccountCommandController {
        private final QueryGateway queryGateway;
        private final BankAccountCommandService bankAccountCommandService;
        private final BankAccountQueryService bankAccountQueryService;
        private final BankAccountRepository bankAccountRepository;

        @Operation(summary = "Create account", description = "Create account need initialBalance", responses = {
                        @ApiResponse(responseCode = "201", description = "Created"),
                        @ApiResponse(responseCode = "400", description = "Validation failed") })
        @PostMapping
        @ResponseStatus(value = HttpStatus.CREATED)
        public CompletableFuture<BankAccountCreatedDTO> createAccount(
                        @Valid @RequestBody BankAccountCreationDTO creationDTO, BindingResult bindingResult)
                        throws Exception {
                log.debug("createAccount creationDTO={}", creationDTO);
                if (bindingResult.hasErrors()) {
                        throw new BindException(bindingResult);
                }
                // conver to command
                CreateAccountCommand createAccountCommand = BankAccountAssembler.toCreateAccountCommand(creationDTO);
                CompletableFuture<String> bankAccountCreateFuture = bankAccountCommandService
                                .createAccount(createAccountCommand);
                // 當取得結果時, 至少執行沒發生錯誤, 可以回覆建立的帳號
                return bankAccountCreateFuture.thenApply(accountNumber -> new BankAccountCreatedDTO(accountNumber));
        }

        @Operation(summary = "deposit Money", description = "deposit Money", responses = {
                        @ApiResponse(responseCode = "200", description = "OK"),
                        @ApiResponse(responseCode = "400", description = "Validation failed") })
        @PostMapping(value = "/{accountNumber}/deposit")
        public CompletableFuture<BankAccountDto> depositMoneyToAccount(
                        @PathVariable(value = "accountNumber") String accountNumber,
                        @Valid @RequestBody MoneyAmountDTO moneyAmountDTO) {
                log.debug("depositMoneyToAccount, accountNumber={}, moneyAmountDTO={}", accountNumber, moneyAmountDTO);
                DepositMoneyCommand depositMoneyCommand = BankAccountAssembler.toDepositMoneyCommand(accountNumber,
                                moneyAmountDTO);
                CompletableFuture<String> depositMoneyFuture = bankAccountCommandService
                                .depositMoney(depositMoneyCommand);
                return depositMoneyFuture.thenApply(depositAccountNumber -> {
                        log.debug("depositAccountNumber={}", depositAccountNumber);
                        BankAccountDto bankAccountDto = null;
                        try {
                                bankAccountDto = this.bankAccountQueryService.findById(accountNumber).get();
                                // bankAccountDto = queryGateway.query(new
                                // FindBankAccountQuery(depositAccountNumber),
                                // ResponseTypes.instanceOf(BankAccountDto.class)).get();
                        } catch (InterruptedException | ExecutionException e) {
                                log.error("查詢失敗", e);
                        }
                        return bankAccountDto;
                });
        }

        @Operation(summary = "withdraw Money", description = "withdraw Money", responses = {
                        @ApiResponse(responseCode = "200", description = "OK"),
                        @ApiResponse(responseCode = "400", description = "Validation failed") })
        @PostMapping(value = "/{accountNumber}/withdraw")
        public CompletableFuture<BankAccountDto> withdrawMoneyFromAccount(
                        @PathVariable(value = "accountNumber") String accountNumber,
                        @Valid @RequestBody MoneyAmountDTO moneyAmountDTO) {
                log.debug("withdrawMoneyFromAccount, accountNumber={}, moneyAmountDTO={}", accountNumber,
                                moneyAmountDTO);
                CompletableFuture<String> withdrawMoneyFuture = bankAccountCommandService.withdrawMoney(
                                BankAccountAssembler.toWithdrawMoneyCommand(accountNumber, moneyAmountDTO));
                // Future 是可以抓到 但訊息也是被封裝過成 CommandExecutionException
                // withdrawMoneyFuture.exceptionally(e -> {
                //         // Corg.axonframework.commandhandling.CommandExecutionException: Insufficient Balance: Cannot debit 500 from account number [806796]
                //         log.error("扣款失敗", e);
                //         return null;
                // });
                return withdrawMoneyFuture.thenApply(withdrawAccountNumber -> {
                        log.debug("withdrawAccountNumber={}", withdrawAccountNumber);
                        BankAccountDto bankAccountDto = null;
                        try {
                                bankAccountDto = this.bankAccountQueryService.findById(accountNumber).get();
                        } catch (InterruptedException | ExecutionException e) {
                                log.error("查詢失敗", e);
                        }
                        return bankAccountDto;
                });
        }
}
