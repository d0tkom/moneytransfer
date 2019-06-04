package service;

import db.DataStore;
import model.Transfer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

public class TransferServiceImpl implements TransferService {
    private final DataStore db;

    public TransferServiceImpl(DataStore db) {
        this.db = db;
    }
    @Override
    public Transfer getTransfer(String id) {
        return db.transfers.get(id);
    }

    @Override
    public Collection<Transfer> getTransfers() {
        return db.transfers.values();
    }

    @Override
    public Transfer transfer(String source, String target, BigDecimal amount) {
        String uuid = UUID.randomUUID().toString();
        Transfer transfer = new Transfer(uuid, source,target, amount);

        db.transfers.put(uuid, transfer);

        return db.transfers.get(uuid);
    }
}
