package service;

import db.DataStore;
import model.Account;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collectors;

public class AccountServiceImpl implements AccountService {
    private final DataStore db;

    public AccountServiceImpl(DataStore db) {
        this.db = db;
    }

    @Override
    public Account getAccount(String id) {
        if (db.accounts.contains(id)) {
            Account acc = getAccount(id);

            return acc;
        }

        return null;
    }

    @Override
    public Collection<Account> getAccounts() {
        return db.accounts.stream().map(a -> getAccountWithBalance(a)).collect(Collectors.toList());
    }

    private Account getAccountWithBalance(String id) {
        BigDecimal balance = db.transfers.values().stream()
                .map(t -> t.source.equals(id) ? t.amount.negate() : t.target.equals(id) ? t.amount : new BigDecimal(0))
                .reduce(new BigDecimal(0), BigDecimal::add);

        return new Account(id, balance);
    }
}
