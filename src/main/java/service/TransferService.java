package service;

import model.Transfer;

import java.math.BigDecimal;
import java.util.Collection;

public interface TransferService {
    Transfer getTransfer(String id);
    Collection<Transfer> getTransfers();
    Transfer transfer(String source, String target, BigDecimal amount);
}
