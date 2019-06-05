package handler;

import model.Account;
import service.AccountService;
import service.TransferService;

import java.math.BigDecimal;
import java.util.Collection;

public class AccountHandler {
    private final AccountService accountService;
    private final TransferService transferService;

    public AccountHandler(AccountService accountService, TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
    }

    public Account getAccount(String id) {
        return accountService.getAccount(id);
    }

    public Account createAccount(BigDecimal balance) {
        String id = accountService.createAccount();

        if (balance != null && balance.compareTo(BigDecimal.ZERO) > -1) {
            transferService.transfer(null, id, balance);
        } else {
            balance = BigDecimal.ZERO;
        }

        return new Account(id, balance);
    }

    public Collection<Account> getAccounts() {
        return accountService.getAccounts();
    }
}
