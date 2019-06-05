package service;

import db.DataStore;
import exception.AccountNotFoundException;
import model.Account;
import org.junit.Before;
import org.junit.Test;
import service.AccountService;
import service.AccountServiceImpl;

import java.util.Collection;
import java.util.List;

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
        String accId = subject.createAccount();

        String storedId = subject.getAccount(accId).id;

        assertEquals(accId, storedId);
    }

    @Test(expected = AccountNotFoundException.class)
    public void accountDoesntExist() {
        String accId = "randomUUID";

        subject.getAccount(accId);
    }

    @Test
    public void createMultipleAccountsNoError() {
        String acc1Id = subject.createAccount();
        String acc2Id = subject.createAccount();
        String acc3Id = subject.createAccount();

        Collection<Account> accounts = subject.getAccounts();

        assertEquals(3, accounts.size());
        assertTrue(accounts.stream().anyMatch(a -> a.id.equals(acc1Id)));
        assertTrue(accounts.stream().anyMatch(a -> a.id.equals(acc2Id)));
        assertTrue(accounts.stream().anyMatch(a -> a.id.equals(acc3Id)));
    }

}
