package handler;

import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import model.AccountResponse;
import model.Transfer;
import service.AccountService;
import service.TransferService;
import validator.Validator;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

public class TransferHandler {
    private AccountService accountService;
    private TransferService transferService;

    private Validator validator = new Validator();

    private ReentrantLock lock = new ReentrantLock();

    public TransferHandler(AccountService accountService, TransferService transferService) {
        this.accountService = accountService;
        this.transferService = transferService;
    }

    public Transfer getTransfer(String id) {
        return transferService.getTransfer(id);
    }

    public Collection<Transfer> getTransfers() {
        return transferService.getTransfers();
    }

    public Transfer transfer(String sourceId, String targetId, BigDecimal amount) throws Exception {
        validator.validateAmount(amount);
        validator.validateAccountsDiffer(sourceId, targetId);

        Exception accountNotFoundException = null;
        Exception insufficientFundsException = null;

        // We create a lock before validating the required balance, and calling transfer().
        // This way we can make sure that the balance won't change while we are validating it.
        // This can also become a bottleneck for our transactions, as only one thread can handle
        // a transfer at any time.
        lock.lock();
        try {
            AccountResponse source = accountService.getAccount(sourceId);
            accountService.getAccount(targetId); // this will throw if target doesn't exist

            validator.validateSufficientBalance(source.balance, amount);

            return transferService.transfer(sourceId, targetId, amount);
        } finally {
            lock.unlock();
        }

    }
}
