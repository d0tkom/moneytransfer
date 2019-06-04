package service;

import model.Account;

import java.util.Collection;

public interface AccountService {
    Account getAccount(String id);
    Collection<Account> getAccounts();
}
