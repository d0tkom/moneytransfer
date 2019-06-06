package db;

import model.Account;
import model.Transfer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    public final Map<String, Account> accounts = new ConcurrentHashMap<>();
    public final Map<String, Transfer> transfers = new ConcurrentHashMap<>();

    public DataStore() {

    }
}
