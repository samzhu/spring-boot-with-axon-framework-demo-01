package com.example.demo.accountms.domain.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyWithdrewEvent {
    private String accountNumber;
    private Integer withdrawAmount;
}
