package db;

import handler.AccountHandler;
import model.Account;
import model.Transfer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    public final Map<String, Account> accounts = new ConcurrentHashMap<>();
    public final Map<String, Transfer> transfers = new ConcurrentHashMap<>();

    public DataStore() {
//        accounts.put("acc1", new Account("acc1", LocalDateTime.now()));
//        accounts.put("acc2", new Account("acc2", LocalDateTime.now()));
//        accounts.put("acc3", new Account("acc2", LocalDateTime.now()));
//
//        Transfer t1 = new Transfer("t1", "acc1", "acc2", new BigDecimal(10000), LocalDateTime.now());
//        transfers.put(t1.id, t1);
    }
}
