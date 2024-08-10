package org.example.bankingapp.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class TransferDto {
    Long fromAccountId;
    Long toAccountId;
    BigDecimal amount;
}
