package service;

import db.DataStore;
import exception.AccountNotFoundException;
import model.Account;
import model.AccountWithBalance;
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
        Account acc = subject.createAccount();

        Account stored = subject.getAccount(acc.id);

        assertEquals(acc.id, stored.id);
        assertEquals(acc.created, stored.created);
    }

    @Test(expected = AccountNotFoundException.class)
    public void accountDoesntExist() {
        String accId = "randomUUID";

        subject.getAccount(accId);
    }

    @Test
    public void createMultipleAccountsNoError() {
        Account acc1 = subject.createAccount();
        Account acc2 = subject.createAccount();
        Account acc3 = subject.createAccount();

        Collection<AccountWithBalance> accounts = subject.getAccounts();

        assertEquals(3, accounts.size());
        assertTrue(accounts.stream().anyMatch(a -> a.id.equals(acc1.id)));
        assertTrue(accounts.stream().anyMatch(a -> a.id.equals(acc2.id)));
        assertTrue(accounts.stream().anyMatch(a -> a.id.equals(acc3.id)));
    }

}
