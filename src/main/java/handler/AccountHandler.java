package handler;

import model.Account;
import model.AccountResponse;
import model.Transfer;
import service.AccountService;
import service.TransferService;
import validator.Validator;

import java.math.BigDecimal;
import java.util.Collection;

public class AccountHandler {
    private final AccountService accountService;
    private final TransferService transferService;

    private Validator validator = new Validator();

    public AccountHandler(AccountService accountService, TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
    }

    public AccountResponse getAccount(String id) {
        return accountService.getAccount(id);
    }

    public AccountResponse createAccount(BigDecimal balance) {
        validator.validateStartingBalance(balance);

        Account account = accountService.createAccount();

        // if balance is null (wasn't provided) we just open an account with zero balance
        if (balance == null) {
            balance = BigDecimal.ZERO;
        } else {
            // if balance was provided, we add initial balance represented as a transfer coming from null
            // (from outside of our system)
            transferService.transfer(null, account.id, balance);
        }

        return new AccountResponse(account.id, account.created, balance);
    }

    public Collection<AccountResponse> getAccounts() {
        return accountService.getAccounts();
    }

    public Collection<Transfer> getTransfersByAccountId(String accountId) {
        return transferService.getTransfersByAccountId(accountId);
    }

}
