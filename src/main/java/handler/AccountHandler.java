package handler;

import model.Account;
import service.AccountService;

import java.util.Collection;

public class AccountHandler {
    private final AccountService accountService;

    public AccountHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    public Account getAccount(String id) {
        return accountService.getAccount(id);
    }

    public Collection<Account> getAccounts() {
        return accountService.getAccounts();
    }
}
