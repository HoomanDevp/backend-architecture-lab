package dev.hooman.lab.transactions.account;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(Long accountId, BigDecimal balance, BigDecimal amount) {
        super("Account %s has balance %s and cannot withdraw %s".formatted(accountId, balance, amount));
    }
}
