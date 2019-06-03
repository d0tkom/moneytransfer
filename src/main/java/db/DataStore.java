package db;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DataStore {
    public final Set<String> accounts = ConcurrentHashMap.newKeySet();
}
