package service;

import model.Account;
import model.AccountWithBalance;

import java.util.Collection;

public interface AccountService {
    AccountWithBalance getAccount(String id);
    Account createAccount();
    Collection<AccountWithBalance> getAccounts();
}
