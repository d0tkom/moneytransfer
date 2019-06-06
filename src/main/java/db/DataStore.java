package db;

import model.Account;
import model.Transfer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    public final Map<String, Account> accounts = new ConcurrentHashMap<>();
    public final Map<String, Transfer> transfers = new ConcurrentHashMap<>();

    public DataStore() {
//        accounts.add("acc1");
//        accounts.add("acc2");
//
//        Transfer t1 = new Transfer("t1", "acc1", "acc2", new BigDecimal(10000));
//        transfers.put(t1.id, t1);
    }
}
