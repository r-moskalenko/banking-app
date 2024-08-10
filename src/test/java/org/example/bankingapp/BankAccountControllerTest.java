package org.example.bankingapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bankingapp.domain.BankAccount;
import org.example.bankingapp.dto.TransferDto;
import org.example.bankingapp.service.BankAccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankAccountControllerTest extends SpringBootApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BankAccountService accountService;

    @Test
    public void testDepositFunds() throws Exception {
        // Setup
        BankAccount bankAccount = accountService.createAccount("Test", BigDecimal.ZERO);
        Long accountId = bankAccount.getId();
        BigDecimal depositAmount = BigDecimal.valueOf(100);

        // Execute and Verify
        mockMvc.perform(post("/api/accounts/{accountId}/deposit", accountId)
                        .param("amount", depositAmount.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Assertions.assertEquals(accountService.getAccountByNumber(bankAccount.getAccountNumber()).getBalance().toString(), "100.00");
    }

    @Test
    public void testWithdrawFunds() throws Exception {
        // Setup
        BankAccount bankAccount = accountService.createAccount("Test", BigDecimal.valueOf(150));
        Long accountId = bankAccount.getId();
        BigDecimal withdrawAmount = BigDecimal.valueOf(50);

        // Execute and Verify
        mockMvc.perform(post("/api/accounts/{accountId}/withdraw", accountId)
                        .param("amount", String.valueOf(withdrawAmount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Assertions.assertEquals(accountService.getAccountByNumber(bankAccount.getAccountNumber()).getBalance().toString(), "100.00");
    }

    @Test
    public void testTransferFunds() throws Exception {
        // Setup
        BankAccount fromAccount = accountService.createAccount("Test", BigDecimal.valueOf(150));
        BankAccount toAccount = accountService.createAccount("Test", BigDecimal.ZERO);
        Long fromAccountId = fromAccount.getId();
        Long toAccountId = toAccount.getId();
        BigDecimal transferAmount = BigDecimal.valueOf(30);

        TransferDto transferDto = TransferDto.builder()
                .fromAccountId(fromAccountId)
                .toAccountId(toAccountId)
                .amount(transferAmount)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String transferJson = mapper.writeValueAsString(transferDto);

        // Execute and Verify
        mockMvc.perform(post("/api/accounts/transfer")
                        .content(transferJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Assertions.assertEquals(accountService.getAccountByNumber(fromAccount.getAccountNumber()).getBalance().toString(), "120.00");
        Assertions.assertEquals(accountService.getAccountByNumber(toAccount.getAccountNumber()).getBalance().toString(), "30.00");
    }
}
