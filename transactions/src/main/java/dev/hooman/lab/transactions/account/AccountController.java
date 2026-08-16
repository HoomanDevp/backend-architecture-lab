package dev.hooman.lab.transactions.account;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepository;
    private final NaiveWithdrawalService withdrawalService;

    public AccountController(AccountRepository accountRepository, NaiveWithdrawalService withdrawalService) {
        this.accountRepository = accountRepository;
        this.withdrawalService = withdrawalService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse create(@RequestBody CreateAccountRequest request) {
        Account account = accountRepository.save(new Account(request.initialBalance()));
        return AccountResponse.from(account);
    }

    @GetMapping("/{id}")
    public AccountResponse get(@PathVariable Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + id));
        return AccountResponse.from(account);
    }

    @PostMapping("/{id}/withdraw")
    public WithdrawalResponse withdraw(@PathVariable Long id, @RequestBody WithdrawalRequest request) {
        BigDecimal balance = withdrawalService.withdraw(id, request.amount());
        return new WithdrawalResponse(id, balance);
    }

    public record CreateAccountRequest(BigDecimal initialBalance) {
    }

    public record WithdrawalRequest(BigDecimal amount) {
    }

    public record AccountResponse(Long id, BigDecimal balance) {
        static AccountResponse from(Account account) {
            return new AccountResponse(account.getId(), account.getBalance());
        }
    }

    public record WithdrawalResponse(Long accountId, BigDecimal balance) {
    }
}
