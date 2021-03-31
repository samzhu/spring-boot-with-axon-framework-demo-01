package com.example.demo.accountms.domain.model;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import com.example.demo.accountms.domain.commands.CreateAccountCommand;
import com.example.demo.accountms.domain.commands.DepositMoneyCommand;
import com.example.demo.accountms.domain.commands.WithdrawMoneyCommand;
import com.example.demo.accountms.domain.events.AccountCreatedEvent;
import com.example.demo.accountms.domain.events.MoneyDepositedEvent;
import com.example.demo.accountms.domain.events.MoneyWithdrewEvent;
import com.example.demo.exceptions.InsufficientBalanceException;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 *  
 * @CommandHandl 註解功能是您決策/業務邏輯的地方
 * 使用 @EventSourcingHandler告訴框架
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Slf4j
@Aggregate(snapshotTriggerDefinition = "snapshotTrigger")
public class BankAccountAggregate {

    @AggregateIdentifier
    private String accountNumber;
    private Integer balance;
    private String customer;

    @CommandHandler
    public BankAccountAggregate(CreateAccountCommand command) {
        log.debug("CommandHandler aggregate={}, command={}", this, command);
        apply(new AccountCreatedEvent(command.getAccountNumber(), command.getInitialBalance(), command.getOwner()));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        log.debug("EventSourcingHandler aggregate={}, event={}", this, event);
        this.accountNumber = event.getAccountNumber();
        this.balance = event.getInitialBalance();
        this.customer = event.getCustomer();
    }

    @CommandHandler
    public void handle(DepositMoneyCommand command) {
        log.debug("CommandHandler aggregate={}, command={}", this, command);
        apply(new MoneyDepositedEvent(command.getAccountNumber(), command.getDepositAmount()));
    }

    @EventSourcingHandler
    public void on(MoneyDepositedEvent event) {
        log.debug("EventSourcingHandler aggregate={}, event={}", this, event);
        this.balance = this.balance + event.getDepositAmount();
    }

    @CommandHandler
    public void handle(WithdrawMoneyCommand command) {
        log.debug("CommandHandler aggregate={}, command={}", this, command);
        if (command.getWithdrawAmount() > this.balance) {
            throw new InsufficientBalanceException(this.accountNumber, command.getWithdrawAmount());
        }
        apply(new MoneyWithdrewEvent(command.getAccountNumber(), command.getWithdrawAmount()));
    }

    @EventSourcingHandler
    public void on(MoneyWithdrewEvent event) {
        log.debug("EventSourcingHandler aggregate={}, event={}", this, event);
        this.balance = this.balance - event.getWithdrawAmount();
    }
}
