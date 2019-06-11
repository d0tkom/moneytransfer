package service;

import db.DataStore;
import exception.AccountNotFoundException;
import exception.TransferNotFoundException;
import model.Account;
import model.Transfer;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransferServiceTest {
    private TransferService subject;
    private DataStore db;

    private final BigDecimal amount = new BigDecimal(1000);

    @Before
    public void setUp() {
        db = new DataStore();

        subject = new TransferServiceImpl(db);
    }

    @Test
    public void createAndGetExistingTransferNoError() {
        Transfer t = subject.transfer("acc1", "acc2", amount);

        Transfer stored = subject.getTransfer(t.id);

        assertEquals(t, stored);
    }

    @Test(expected = TransferNotFoundException.class)
    public void accountDoesntExist() {
        String tId = "randomUUID";

        subject.getTransfer(tId);
    }

    @Test
    public void createMultipleTransfersNoError() {
        Transfer t1 = subject.transfer("acc1", "acc2", amount);
        Transfer t2 = subject.transfer("acc1", "acc2", amount);
        Transfer t3 = subject.transfer("acc1", "acc2", amount);

        Collection<Transfer> transfers = subject.getTransfers();

        assertEquals(3, transfers.size());
        assertTrue(transfers.stream().anyMatch(a -> a.equals(t1)));
        assertTrue(transfers.stream().anyMatch(a -> a.equals(t2)));
        assertTrue(transfers.stream().anyMatch(a -> a.equals(t3)));
    }

    @Test
    public void getTransfersByAccountId() {
        db.accounts.put("acc1", new Account("acc1", LocalDateTime.now()));
        db.accounts.put("acc2", new Account("acc1", LocalDateTime.now()));
        db.accounts.put("acc3", new Account("acc1", LocalDateTime.now()));

        Transfer t1 = subject.transfer("acc1", "acc2", amount);
        Transfer t2 = subject.transfer("acc1", "acc2", amount);
        Transfer t3 = subject.transfer("acc1", "acc2", amount);

        Transfer t4 = subject.transfer("acc1", "acc3", amount);

        Transfer t5 = subject.transfer("dummy", "dummy2", amount);
        Transfer t6 = subject.transfer("dummy", "dummy2", amount);

        Collection<Transfer> acc1Transfers = subject.getTransfersByAccountId("acc1");
        Collection<Transfer> acc2Transfers = subject.getTransfersByAccountId("acc2");
        Collection<Transfer> acc3Transfers = subject.getTransfersByAccountId("acc3");

        assertEquals(4, acc1Transfers.size());
        assertEquals(3, acc2Transfers.size());
        assertEquals(1, acc3Transfers.size());
        assertTrue(acc1Transfers.stream().anyMatch(a -> a.equals(t1)));
        assertTrue(acc1Transfers.stream().anyMatch(a -> a.equals(t2)));
        assertTrue(acc1Transfers.stream().anyMatch(a -> a.equals(t3)));
        assertTrue(acc1Transfers.stream().anyMatch(a -> a.equals(t4)));
        assertTrue(acc2Transfers.stream().anyMatch(a -> a.equals(t1)));
        assertTrue(acc2Transfers.stream().anyMatch(a -> a.equals(t2)));
        assertTrue(acc2Transfers.stream().anyMatch(a -> a.equals(t3)));
        assertTrue(acc3Transfers.stream().anyMatch(a -> a.equals(t4)));
        assertTrue(acc1Transfers.stream().noneMatch(a -> a.equals(t5)));
        assertTrue(acc1Transfers.stream().noneMatch(a -> a.equals(t6)));
        assertTrue(acc2Transfers.stream().noneMatch(a -> a.equals(t5)));
        assertTrue(acc2Transfers.stream().noneMatch(a -> a.equals(t6)));
        assertTrue(acc3Transfers.stream().noneMatch(a -> a.equals(t5)));
        assertTrue(acc3Transfers.stream().noneMatch(a -> a.equals(t6)));
    }

    @Test (expected = AccountNotFoundException.class)
    public void getTransfersByAccountIdThatDoesntExist() {
        subject.getTransfersByAccountId("acc1");
    }
}
