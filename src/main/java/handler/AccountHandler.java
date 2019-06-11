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

        if (balance == null) {
            balance = BigDecimal.ZERO;
        } else {
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
