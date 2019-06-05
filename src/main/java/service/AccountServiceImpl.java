package service;

import db.DataStore;
import exception.AccountNotFoundException;
import javafx.util.Pair;
import model.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        if (db.accounts.containsKey(id)) {
            LocalDateTime created = db.accounts.get(id);
            return getAccountWithBalance(id, created);
        } else {
            throw new AccountNotFoundException("Account " + id + " not found");
        }
    }

    @Override
    public Pair<String, LocalDateTime> createAccount() {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime created = LocalDateTime.now();

        db.accounts.put(uuid, created);

        return new Pair<>(uuid, created);
    }

    @Override
    public Collection<Account> getAccounts() {
        return db.accounts.entrySet().stream().map((e) -> getAccountWithBalance(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    private Account getAccountWithBalance(String id, LocalDateTime created) {
        BigDecimal balance = db.transfers.values().stream()
                .map(t -> Objects.equals(t.source, id) ? t.amount.negate() : Objects.equals(t.target, id) ? t.amount : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Account(id, balance, created);
    }
}
