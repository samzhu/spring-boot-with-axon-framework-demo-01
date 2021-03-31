package com.example.demo.accountms.domain.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepositMoneyCommand {
    @TargetAggregateIdentifier
    private String accountNumber;
    private Integer depositAmount;
}
