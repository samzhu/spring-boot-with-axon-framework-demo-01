package com.example.demo.accountms.application.internal.querygateways;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.example.demo.accountms.domain.queries.FindBankAccountQuery;
import com.example.demo.accountms.interfaces.rest.dto.BankAccountDto;

import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.Message;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class BankAccountQueryService {
    private final QueryGateway queryGateway;
    private final EventStore eventStore;

    public CompletableFuture<BankAccountDto> findById(String accountNumber) {
        log.debug("BankAccountQueryService.findById {}");
        return this.queryGateway.query(
                new FindBankAccountQuery(accountNumber),
                ResponseTypes.instanceOf(BankAccountDto.class)
        );
    }

    public List<Object> listEventsForAccount(String accountNumber) {
        return this.eventStore
                .readEvents(accountNumber)
                .asStream()
                .map(Message::getPayload)
                .collect(Collectors.toList());
    }
}
