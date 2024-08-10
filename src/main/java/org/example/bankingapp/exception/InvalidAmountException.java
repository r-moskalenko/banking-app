package org.example.bankingapp.exception;

import java.math.BigDecimal;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException(String issue, BigDecimal amount, String cause) {
        super(issue + ", amount " + amount + cause);
    }
}
