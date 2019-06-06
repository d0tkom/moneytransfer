package service;

import db.DataStore;
import exception.AccountNotFoundException;
import exception.TransferNotFoundException;
import model.Account;
import model.Transfer;
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

public class TransferServiceTest {
    private TransferService subject;

    @Before
    public void setUp() {
        DataStore db = new DataStore();

//        db.accounts.add("acc1");
//        db.accounts.add("acc2");
//
//        db.transfers.put("t1", new Transfer("t1", null, "acc1", new BigDecimal(1000)));

        subject = new TransferServiceImpl(db);
    }

    @Test
    public void createAndGetExistingTransferNoError() {
        Transfer t = subject.transfer("acc1", "acc2", new BigDecimal(1000));

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
        Transfer t1 = subject.transfer("acc1", "acc2", new BigDecimal(1000));
        Transfer t2 = subject.transfer("acc1", "acc2", new BigDecimal(1000));
        Transfer t3 = subject.transfer("acc1", "acc2", new BigDecimal(1000));

        Collection<Transfer> transfers = subject.getTransfers();

        assertEquals(3, transfers.size());
        assertTrue(transfers.stream().anyMatch(a -> a.equals(t1)));
        assertTrue(transfers.stream().anyMatch(a -> a.equals(t2)));
        assertTrue(transfers.stream().anyMatch(a -> a.equals(t3)));
    }

    @Test
    public void getTransfersByAccountId() {
        Transfer t1 = subject.transfer("acc1", "acc2", new BigDecimal(1000));
        Transfer t2 = subject.transfer("acc1", "acc2", new BigDecimal(1000));
        Transfer t3 = subject.transfer("acc1", "acc2", new BigDecimal(1000));

        Transfer t4 = subject.transfer("acc1", "acc3", new BigDecimal(1000));

        Transfer t5 = subject.transfer("dummy", "dummy2", new BigDecimal(1000));
        Transfer t6 = subject.transfer("dummy", "dummy2", new BigDecimal(1000));

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

}
