package org.example.bankingapp.service;

import org.example.bankingapp.domain.BankAccount;
import org.example.bankingapp.dto.TransferDto;
import org.example.bankingapp.exception.AccountNotFoundException;
import org.example.bankingapp.exception.InsufficientFundsException;
import org.example.bankingapp.exception.InvalidAmountException;
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
        if (initialBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Cannot create account", initialBalance, " cannot be negative");
        }
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
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Cannot deposit", amount, " should be positive");
        }
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        account.deposit(amount);
        accountRepository.save(account);
    }

    public void withdraw(Long accountId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Cannot withdraw", amount, " should be positive");
        }
        BankAccount account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(amount, account.getBalance());
        }
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
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Cannot transfer", amount, " should be positive");
        }
        BankAccount fromAccount = accountRepository.findById(fromAccountId)
                .orElseThrow(() -> new AccountNotFoundException(fromAccountId));
        BankAccount toAccount = accountRepository.findById(toAccountId)
                .orElseThrow(() -> new AccountNotFoundException(toAccountId));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException(amount, fromAccount.getBalance());
        }
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}
