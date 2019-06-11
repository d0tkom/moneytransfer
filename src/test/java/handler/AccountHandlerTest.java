package handler;

import db.DataStore;
import exception.AccountNotFoundException;
import exception.InvalidAmountException;
import model.Account;
import model.AccountResponse;
import org.junit.Before;
import org.junit.Test;
import service.AccountService;
import service.AccountServiceImpl;
import service.TransferService;
import service.TransferServiceImpl;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AccountHandlerTest {
    private AccountHandler subject;

    @Before
    public void setUp() {
        DataStore db = new DataStore();

        AccountService accountService = new AccountServiceImpl(db);
        TransferService transferService = new TransferServiceImpl(db);

        subject = new AccountHandler(accountService, transferService);
    }

    @Test
    public void testCreateAccountZeroBalance() {
        Account acc = subject.createAccount(BigDecimal.ZERO);

        AccountResponse stored = subject.getAccount(acc.id);

        assertEquals(stored, acc);
        assertEquals(stored.balance, BigDecimal.ZERO);
    }

    @Test
    public void testCreateAccountWithBalance() {
        Account acc = subject.createAccount(new BigDecimal(100));

        AccountResponse stored = subject.getAccount(acc.id);

        assertEquals(stored, acc);
        assertEquals(stored.balance, new BigDecimal(100));
    }

    @Test
    public void testCreateAccountNullBalance() {
        Account acc = subject.createAccount(null);

        AccountResponse stored = subject.getAccount(acc.id);

        assertEquals(stored, acc);
        assertEquals(stored.balance, BigDecimal.ZERO);
    }

    @Test (expected = InvalidAmountException.class)
    public void testCreateAccountNegativeBalance() {
        Account acc = subject.createAccount(new BigDecimal(-100));
    }

    @Test(expected = AccountNotFoundException.class)
    public void accountDoesntExist() {
        String accId = "randomUUID";

        subject.getAccount(accId);
    }

    @Test
    public void createMultipleAccountsNoError() {
        Account acc1 = subject.createAccount(BigDecimal.ZERO);
        Account acc2 = subject.createAccount(new BigDecimal(100));
        Account acc3 = subject.createAccount(null);

        Collection<AccountResponse> accounts = subject.getAccounts();

        assertEquals(3, accounts.size());
        assertTrue(accounts.stream().anyMatch(a -> a.equals(acc1)));
        assertTrue(accounts.stream().anyMatch(a -> a.equals(acc2)));
        assertTrue(accounts.stream().anyMatch(a -> a.equals(acc3)));
    }
}

