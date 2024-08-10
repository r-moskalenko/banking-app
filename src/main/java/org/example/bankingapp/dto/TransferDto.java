package org.example.bankingapp.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferDto {
    Long fromAccountId;
    Long toAccountId;
    BigDecimal amount;
}
