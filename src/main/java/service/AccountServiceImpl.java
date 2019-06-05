package service;

import db.DataStore;
import exception.AccountNotFoundException;
import model.Account;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {
    private final DataStore db;

    public AccountServiceImpl(DataStore db) {
        this.db = db;
    }

    @Override
    public Account getAccount(String id) {
        if (db.accounts.contains(id)) {
            return getAccountWithBalance(id);
        } else {
            throw new AccountNotFoundException("Account " + id + " not found");
        }
    }

    @Override
    public String createAccount() {
        String uuid = UUID.randomUUID().toString();

        db.accounts.add(uuid);

        return uuid;
    }

    @Override
    public Collection<Account> getAccounts() {
        return db.accounts.stream().map(a -> getAccountWithBalance(a)).collect(Collectors.toList());
    }

    private Account getAccountWithBalance(String id) {
        BigDecimal balance = db.transfers.values().stream()
                .map(t -> Objects.equals(t.source, id) ? t.amount.negate() : Objects.equals(t.target, id) ? t.amount : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Account(id, balance);
    }
}
