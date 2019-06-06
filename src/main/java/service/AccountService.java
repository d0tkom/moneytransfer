package service;

import model.Account;
import model.AccountResponse;

import java.util.Collection;

public interface AccountService {
    AccountResponse getAccount(String id);
    Account createAccount();
    Collection<AccountResponse> getAccounts();
}
