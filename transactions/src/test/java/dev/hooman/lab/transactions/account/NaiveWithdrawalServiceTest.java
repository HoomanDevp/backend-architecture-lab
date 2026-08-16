package dev.hooman.lab.transactions.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class NaiveWithdrawalServiceTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private NaiveWithdrawalService withdrawalService;

    @BeforeEach
    void cleanDatabase() {
        accountRepository.deleteAll();
    }

    @Test
    void withdrawsMoneyFromAccount() {
        Account account = accountRepository.save(new Account(new BigDecimal("1000.00")));

        BigDecimal remainingBalance = withdrawalService.withdraw(account.getId(), new BigDecimal("300.00"));

        assertThat(remainingBalance).isEqualByComparingTo("700.00");
        assertThat(accountRepository.findById(account.getId()).orElseThrow().getBalance())
                .isEqualByComparingTo("700.00");
    }

    @Test
    void rejectsWithdrawalWhenBalanceIsInsufficient() {
        Account account = accountRepository.save(new Account(new BigDecimal("500.00")));

        assertThatThrownBy(() -> withdrawalService.withdraw(account.getId(), new BigDecimal("700.00")))
                .isInstanceOf(InsufficientFundsException.class);

        assertThat(accountRepository.findById(account.getId()).orElseThrow().getBalance())
                .isEqualByComparingTo("500.00");
    }
}
