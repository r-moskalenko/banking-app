package org.example.bankingapp.repository;

import org.example.bankingapp.domain.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.security.auth.login.AccountException;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    BankAccount findByAccountNumber(String accountNumber);
}
