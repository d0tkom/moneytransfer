package handler;

import javafx.util.Pair;
import model.Account;
import service.AccountService;
import service.TransferService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
        Pair<String, LocalDateTime> account = accountService.createAccount();

        String id = account.getKey();
        LocalDateTime created = account.getValue();

        if (balance != null && balance.compareTo(BigDecimal.ZERO) > -1) {
            transferService.transfer(null, id, balance);
        } else {
            balance = BigDecimal.ZERO;
        }

        return new Account(id, balance, created);
    }

    public Collection<Account> getAccounts() {
        return accountService.getAccounts();
    }
}
