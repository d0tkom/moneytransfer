package service;

import db.DataStore;
import exception.AccountNotFoundException;
import model.Account;
import model.AccountResponse;

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
    public AccountResponse getAccount(String id) {
        if (db.accounts.containsKey(id)) {
            LocalDateTime created = db.accounts.get(id).created;

            return getAccountWithBalance(id, created);
        } else {
            throw new AccountNotFoundException("Account " + id + " not found");
        }
    }

    @Override
    public Account createAccount() {
        String uuid = UUID.randomUUID().toString();
        LocalDateTime created = LocalDateTime.now();

        Account account = new Account(uuid, created);

        db.accounts.put(uuid, account);

        return account;
    }

    @Override
    public Collection<AccountResponse> getAccounts() {
        return db.accounts.values().stream().map((a) -> getAccountWithBalance(a.id, a.created)).collect(Collectors.toList());
    }

    private AccountResponse getAccountWithBalance(String id, LocalDateTime created) {
        BigDecimal balance = db.transfers.values().stream()
                .map(t -> Objects.equals(t.source, id) ? t.amount.negate() : Objects.equals(t.target, id) ? t.amount : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AccountResponse(id, created, balance);
    }
}
