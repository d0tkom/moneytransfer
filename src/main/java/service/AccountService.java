package service;

import javafx.util.Pair;
import model.Account;

import java.time.LocalDateTime;
import java.util.Collection;

public interface AccountService {
    Account getAccount(String id);
    Pair<String, LocalDateTime> createAccount();
    Collection<Account> getAccounts();
}
