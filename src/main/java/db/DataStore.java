package db;

import model.Transfer;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    public final Set<String> accounts = ConcurrentHashMap.newKeySet();
    public final Map<String, Transfer> transfers = new ConcurrentHashMap<>();

    public DataStore() {
//        accounts.add("acc1");
//        accounts.add("acc2");
//
//        Transfer t1 = new Transfer("t1", "acc1", "acc2", new BigDecimal(10000));
//        transfers.put(t1.id, t1);
    }
}
