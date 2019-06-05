package service;

import db.DataStore;
import exception.TransferNotFoundException;
import model.Transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

public class TransferServiceImpl implements TransferService {
    private final DataStore db;

    public TransferServiceImpl(DataStore db) {
        this.db = db;
    }
    @Override
    public Transfer getTransfer(String id) {
        Transfer transfer = db.transfers.get(id);
        if (transfer != null) {
            return transfer;
        } else {
            throw new TransferNotFoundException("Transfer " + id + " not found");
        }
    }

    @Override
    public Collection<Transfer> getTransfers() {
        return db.transfers.values();
    }

    @Override
    public Transfer transfer(String source, String target, BigDecimal amount) {
        String uuid = UUID.randomUUID().toString();
        Transfer transfer = new Transfer(uuid, source,target, amount, LocalDateTime.now());

        db.transfers.put(uuid, transfer);

        return db.transfers.get(uuid);
    }
}
