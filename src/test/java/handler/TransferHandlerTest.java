package handler;

import db.DataStore;
import exception.AccountNotFoundException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import exception.InvalidTransferException;
import model.Account;
import model.AccountResponse;
import model.Transfer;
import org.junit.Before;
import org.junit.Test;
import service.AccountService;
import service.AccountServiceImpl;
import service.TransferService;
import service.TransferServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class TransferHandlerTest {
    private TransferHandler subject;
    private AccountService accountService;
    private BigDecimal acc1Balance = new BigDecimal(1000);
    private BigDecimal acc2Balance = BigDecimal.ZERO;

    @Before
    public void setUp() {
        DataStore db = new DataStore();

        db.accounts.put("acc1", new Account("acc1", LocalDateTime.now()));
        db.accounts.put("acc2",  new Account("acc2", LocalDateTime.now()));

        db.transfers.put("id1", new Transfer("id1", null, "acc1", acc1Balance, LocalDateTime.now()));

        accountService = new AccountServiceImpl(db);
        TransferService transferService = new TransferServiceImpl(db);

        subject = new TransferHandler(accountService, transferService);
    }

    @Test
    public void transferNoError() throws Exception {
        Transfer t = subject.transfer("acc1", "acc2", new BigDecimal(100));

        Transfer stored = subject.getTransfer(t.id);

        AccountResponse acc1 = accountService.getAccount("acc1");
        AccountResponse acc2 = accountService.getAccount("acc2");

        assertEquals(t, stored);
        assertEquals(acc1Balance.add(new BigDecimal(-100)), acc1.balance);
        assertEquals(acc2Balance.add(new BigDecimal(100)), acc2.balance);
    }

    @Test (expected = InvalidAmountException.class)
    public void transferNegative() throws Exception {
        subject.transfer("acc1", "acc2", new BigDecimal(-100));
    }

    @Test (expected = InvalidAmountException.class)
    public void transferZero() throws Exception {
        subject.transfer("acc1", "acc2", BigDecimal.ZERO);
    }

    @Test (expected = InvalidAmountException.class)
    public void transferNull() throws Exception {
        subject.transfer("acc1", "acc2", null);
    }

    @Test (expected = AccountNotFoundException.class)
    public void sourceAccountDoesntExist() throws Exception {
        subject.transfer("doesntExist", "acc2", new BigDecimal(100));
    }

    @Test (expected = AccountNotFoundException.class)
    public void targetAccountDoesntExist() throws Exception {
        subject.transfer("acc1", "doesntExist", new BigDecimal(100));
    }

    @Test (expected = InvalidTransferException.class)
    public void sendMoneyToSameAccount() throws Exception {
        subject.transfer("acc1", "acc1", new BigDecimal(100));
    }

    @Test
    public void transferBackAndForth() throws Exception {
        Transfer t1 = subject.transfer("acc1", "acc2", new BigDecimal(100));
        Transfer t2 = subject.transfer("acc2", "acc1", new BigDecimal(100));

        Transfer stored1 = subject.getTransfer(t1.id);
        Transfer stored2 = subject.getTransfer(t2.id);

        Collection<Transfer> transfers = subject.getTransfers();

        AccountResponse acc1 = accountService.getAccount("acc1");
        AccountResponse acc2 = accountService.getAccount("acc2");

        assertEquals(t1, stored1);
        assertEquals(t2, stored2);
        assertEquals(3, transfers.size());
        assertEquals(acc1Balance, acc1.balance);
        assertEquals(acc2Balance, acc2.balance);
    }

    @Test(expected = InsufficientFundsException.class)
    public void transferInsufficientFunds() throws Exception {
        subject.transfer("acc1", "acc2", new BigDecimal(10000));
    }
}
