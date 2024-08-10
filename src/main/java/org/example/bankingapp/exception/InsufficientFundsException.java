package org.example.bankingapp.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(BigDecimal toWithdraw, BigDecimal availableBalance) {
        super("Insufficient funds to withdraw $" + toWithdraw + ", available $" + availableBalance);
    }
}
