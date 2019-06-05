package service;

import db.DataStore;
import exception.AccountNotFoundException;
import javafx.util.Pair;
import model.Account;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountServiceTest {
    private AccountService subject;

    @Before
    public void setUp() {
        DataStore db = new DataStore();

        subject = new AccountServiceImpl(db);
    }

    @Test
    public void createAndGetExistingAccountNoError() {
        Pair<String, LocalDateTime> acc = subject.createAccount();
        String accId = acc.getKey();
        LocalDateTime created = acc.getValue();

        Account stored = subject.getAccount(accId);

        assertEquals(accId, stored.id);
        assertEquals(created, stored.created);
    }

    @Test(expected = AccountNotFoundException.class)
    public void accountDoesntExist() {
        String accId = "randomUUID";

        subject.getAccount(accId);
    }

    @Test
    public void createMultipleAccountsNoError() {
        Pair<String, LocalDateTime> acc1 = subject.createAccount();
        Pair<String, LocalDateTime> acc2 = subject.createAccount();
        Pair<String, LocalDateTime> acc3 = subject.createAccount();

        String acc1Id = acc1.getKey();
        String acc2Id = acc2.getKey();
        String acc3Id = acc3.getKey();

        Collection<Account> accounts = subject.getAccounts();

        assertEquals(3, accounts.size());
        assertTrue(accounts.stream().anyMatch(a -> a.id.equals(acc1Id)));
        assertTrue(accounts.stream().anyMatch(a -> a.id.equals(acc2Id)));
        assertTrue(accounts.stream().anyMatch(a -> a.id.equals(acc3Id)));
    }

}
