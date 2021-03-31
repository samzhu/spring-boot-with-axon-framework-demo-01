package com.example.demo.accountms.interfaces.eventhandlers;

import javax.security.auth.login.AccountNotFoundException;

import com.example.demo.accountms.domain.events.AccountCreatedEvent;
import com.example.demo.accountms.domain.events.MoneyDepositedEvent;
import com.example.demo.accountms.domain.events.MoneyWithdrewEvent;
import com.example.demo.accountms.domain.model.BankAccountAggregate;
import com.example.demo.accountms.domain.queries.FindBankAccountQuery;
import com.example.demo.accountms.infrastructure.BankAccountRepository;
import com.example.demo.accountms.interfaces.rest.dto.BankAccountDto;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.modelling.command.Aggregate;
import org.axonframework.modelling.command.AggregateNotFoundException;
import org.axonframework.modelling.command.Repository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Event Handlers for all events by the BankAccount Aggregate
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountEventHandler {
    private final Repository<BankAccountAggregate> bankAccountEventRepository;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.debug("EventHandler AccountCreatedEvent = {}", event);
        String sql = "INSERT INTO bank_account (account_number, balance, customer, created_by, created_date, last_modified_by, last_modified_date) "
                + " VALUES (:accountNumber, :balance, :customer, :customer, CURRENT_TIMESTAMP, :customer, CURRENT_TIMESTAMP)";
        try {
            log.debug("read Aggregate from EventStore start, aggregateID = {}", event.getAccountNumber());
            Aggregate<BankAccountAggregate> bankAccountAggregate = bankAccountEventRepository
                    .load(event.getAccountNumber());
            log.debug("read Aggregate from EventStore End, aggregate identifier = {}", bankAccountAggregate.identifierAsString());
            bankAccountAggregate.execute(bankAccount -> {
                MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
                sqlParameterSource.addValue("accountNumber", bankAccount.getAccountNumber());
                sqlParameterSource.addValue("balance", bankAccount.getBalance());
                sqlParameterSource.addValue("customer", bankAccount.getCustomer());
                namedParameterJdbcTemplate.update(sql, sqlParameterSource);
                log.debug("sql save BankAccount end");

            });
        } catch (AggregateNotFoundException exception) {
            log.error("can't find account number {}", event.getAccountNumber(), exception);
        }
    }

    @EventHandler
    public void on(MoneyDepositedEvent event) throws AccountNotFoundException {
        String sql = "UPDATE bank_account SET balance=:balance WHERE account_number=:accountNumber";
        log.debug("Handling a Bank Account Credit event {}", event);
        try {
            Aggregate<BankAccountAggregate> bankAccountAggregate = bankAccountEventRepository
                    .load(event.getAccountNumber());
            bankAccountAggregate.execute(bankAccount -> {
                MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
                sqlParameterSource.addValue("accountNumber", bankAccount.getAccountNumber());
                sqlParameterSource.addValue("balance", bankAccount.getBalance());
                namedParameterJdbcTemplate.update(sql, sqlParameterSource);
            });

        } catch (AggregateNotFoundException exception) {
            log.error("can't find account number {}", event.getAccountNumber(), exception);
        }
    }

    @EventHandler
    public void on(MoneyWithdrewEvent event) throws AccountNotFoundException {
        String sql = "UPDATE bank_account SET balance=:balance WHERE account_number=:accountNumber";
        log.debug("Handling a Bank Account Credit event {}", event);
        try {
            Aggregate<BankAccountAggregate> bankAccountAggregate = bankAccountEventRepository
                    .load(event.getAccountNumber());
            bankAccountAggregate.execute(bankAccount -> {
                MapSqlParameterSource sqlParameterSource = new MapSqlParameterSource();
                sqlParameterSource.addValue("accountNumber", bankAccount.getAccountNumber());
                sqlParameterSource.addValue("balance", bankAccount.getBalance());
                namedParameterJdbcTemplate.update(sql, sqlParameterSource);
            });

        } catch (AggregateNotFoundException exception) {
            log.error("can't find account number {}", event.getAccountNumber(), exception);
        }
    }

    @QueryHandler
    public BankAccountDto handle(FindBankAccountQuery query) {
        log.debug("Handling FindBankAccountQuery query: {}", query);
        BankAccountDto bankAccountDto = new BankAccountDto();
        Aggregate<BankAccountAggregate> bankAccountAggregate = bankAccountEventRepository
                .load(query.getAccountNumber());
        bankAccountAggregate.execute(aggregate -> {
            bankAccountDto.setAccountNumber(aggregate.getAccountNumber());
            bankAccountDto.setBalance(aggregate.getBalance());
            bankAccountDto.setCustomer(aggregate.getCustomer());
            // BankAccountDto bankAccountDto = new
            // BankAccountDto(aggregate.getAccountNumber(), aggregate.getBalance(),
            // aggregate.getCustomer());
        });
        return bankAccountDto;
        // return bankAccountRepository.findById(query.getAccountNumber()).orElse(null);
    }

}
