package handler;

import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import exception.InvalidTransferException;
import model.AccountResponse;
import model.Transfer;
import service.AccountService;
import service.TransferService;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;

public class TransferHandler {
    private AccountService accountService;
    private TransferService transferService;

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
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount: " + amount + " is not valid");
        } else if (sourceId == targetId) {
            throw new InvalidTransferException("Money cannot be sent to same account");
        }

        Transfer transfer = null;
        Exception accountNotFoundException = null;

        lock.lock();
        try {
            AccountResponse source = accountService.getAccount(sourceId);
            accountService.getAccount(targetId); // this will throw if target doesn't exist

            if (source.balance.compareTo(amount) > -1) {
                transfer = transferService.transfer(sourceId, targetId, amount);
            }
        } catch(AccountNotFoundException e) {
            accountNotFoundException = e;
        } finally {
            lock.unlock();

            if (accountNotFoundException != null)
                throw accountNotFoundException;

            if (transfer == null) {
                throw new InsufficientFundsException("Insufficient funds");
            }

            return transfer;
        }

    }
}
