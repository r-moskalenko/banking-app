package org.example.bankingapp.service;

import org.example.bankingapp.domain.BankAccount;
import org.example.bankingapp.dto.TransferDto;
import org.example.bankingapp.repository.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class BankAccountService {
    private final BankAccountRepository accountRepository;

    public BankAccountService(BankAccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<BankAccount> getAllAccounts() {
        return accountRepository.findAll();
    }

    public BankAccount createAccount(String ownerName, BigDecimal initialBalance) {
        BankAccount account = new BankAccount();
        account.setOwnerName(ownerName);
        account.setBalance(initialBalance);
        account.setAccountNumber(generateAccountNumber());
        return accountRepository.save(account);
    }

    private String generateAccountNumber() {
        return UUID.randomUUID().toString();
    }

    public void deposit(Long accountId, BigDecimal amount) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.deposit(amount);
        accountRepository.save(account);
    }

    public void withdraw(Long accountId, BigDecimal amount) {
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.withdraw(amount);
        accountRepository.save(account);
    }

    public BankAccount getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public void transfer(TransferDto transferDto) {
        Long fromAccountId = transferDto.getFromAccountId();
        Long toAccountId = transferDto.getToAccountId();
        BigDecimal amount = transferDto.getAmount();
        BankAccount fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        BankAccount toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}
