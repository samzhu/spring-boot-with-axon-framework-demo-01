package com.example.demo.accountms.interfaces.rest.transform;

import com.example.demo.accountms.domain.commands.CreateAccountCommand;
import com.example.demo.accountms.domain.commands.DepositMoneyCommand;
import com.example.demo.accountms.domain.commands.WithdrawMoneyCommand;
import com.example.demo.accountms.interfaces.rest.dto.BankAccountCreationDTO;
import com.example.demo.accountms.interfaces.rest.dto.MoneyAmountDTO;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Assembler class to convert the Account Resource Data to the Account Model
 */
public class BankAccountAssembler {

    public static CreateAccountCommand toCreateAccountCommand(BankAccountCreationDTO creationDTO) {
        return new CreateAccountCommand(RandomStringUtils.randomNumeric(6), creationDTO.getInitialBalance(),
                creationDTO.getCustomer());
    }

    public static DepositMoneyCommand toDepositMoneyCommand(String accountNumber, MoneyAmountDTO moneyAmountDTO) {
        return new DepositMoneyCommand(accountNumber, moneyAmountDTO.getAmount());
    }

    public static WithdrawMoneyCommand toWithdrawMoneyCommand(String accountNumber, MoneyAmountDTO moneyAmountDTO) {
        return new WithdrawMoneyCommand(accountNumber, moneyAmountDTO.getAmount());
    }
}
