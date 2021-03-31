package com.example.demo.accountms.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyDepositedEvent {
    private String accountNumber;
    private Integer depositAmount;
}
