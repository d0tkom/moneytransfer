package handler;

import exception.InsufficientFundsException;
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

    public Transfer transfer(String sourceId, String targetId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        Transfer transfer = null;

        lock.lock();
        try {
            AccountResponse source = accountService.getAccount(sourceId);
            if (source != null && source.balance.compareTo(amount) > -1) {
                transfer = transferService.transfer(sourceId, targetId, amount);
            }
        } finally {
            lock.unlock();
            if (transfer == null) {
                throw new InsufficientFundsException("Insufficient funds");
            }
            return transfer;
        }

    }
}
