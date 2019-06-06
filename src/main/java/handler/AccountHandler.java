package handler;

import model.Account;
import model.AccountWithBalance;
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

    public AccountWithBalance getAccount(String id) {
        return accountService.getAccount(id);
    }

    public AccountWithBalance createAccount(BigDecimal balance) {
        Account account = accountService.createAccount();

        if (balance != null && balance.compareTo(BigDecimal.ZERO) > -1) {
            transferService.transfer(null, account.id, balance);
        } else {
            balance = BigDecimal.ZERO;
        }

        return new AccountWithBalance(account.id, account.created, balance);
    }

    public Collection<AccountWithBalance> getAccounts() {
        return accountService.getAccounts();
    }
}
