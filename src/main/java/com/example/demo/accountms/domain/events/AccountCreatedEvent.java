package com.example.demo.accountms.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountCreatedEvent {
    private String accountNumber;
    private Integer initialBalance;
    private String customer;
}
