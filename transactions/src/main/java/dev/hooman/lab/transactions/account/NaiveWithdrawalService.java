package dev.hooman.lab.transactions.account;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class NaiveWithdrawalService {

    private final AccountRepository accountRepository;

    public NaiveWithdrawalService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public BigDecimal withdraw(Long accountId, BigDecimal amount) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));

        account.withdraw(amount);
        return account.getBalance();
    }
}
