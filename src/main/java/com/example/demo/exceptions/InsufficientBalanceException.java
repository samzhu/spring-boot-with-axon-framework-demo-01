package com.example.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class InsufficientBalanceException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public InsufficientBalanceException(String accountNumber, Integer debitAmount) {
                super("Insufficient Balance: Cannot debit " + debitAmount + " from account number [" + accountNumber
                                + "]");
        }
}