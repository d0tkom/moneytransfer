package service;

import model.Transfer;

import java.math.BigDecimal;
import java.util.Collection;

public interface TransferService {
    Transfer getTransfer(String id);
    Collection<Transfer> getTransfers();
    Collection<Transfer> getTransfersByAccountId(String accountId);
    Transfer transfer(String source, String target, BigDecimal amount);
}
