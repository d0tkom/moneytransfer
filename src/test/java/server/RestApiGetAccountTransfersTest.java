package server;

import db.DataStore;
import model.AccountResponse;
import model.Transfer;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import util.RestClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestApiGetAccountTransfersTest {
    private final String jsonMimeType = "application/json";
    private final String url = "http://localhost:4567/accounts/";

    private static RestClient restClient;
    private static DataStore db;

    @BeforeClass
    public static void startServer() {
        restClient = new RestClient();

        db = new DataStore();
        RestApi api = new RestApi(db);

        api.listen();
    }

    @After
    public void clearDb() {
        db.transfers.clear();
        db.accounts.clear();
    }

    @Test
    public void getAccountTransfersByIdReturns200() throws IOException {
        AccountResponse account = restClient.postAccount();

        HttpUriRequest request = new HttpGet(url + account.id + "/transfers");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getAccountTransfersByIdReturnsJson() throws IOException {
        AccountResponse account = restClient.postAccount();

        HttpUriRequest request = new HttpGet(url + account.id + "/transfers");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(jsonMimeType, ContentType.getOrDefault(response.getEntity()).getMimeType());
    }

    @Test
    public void getAccountTransfersReturnsTransfersForGivenAccount() throws IOException {
        AccountResponse acc1 = restClient.postAccountWithBalance(new BigDecimal(1000));
        AccountResponse acc2 = restClient.postAccount();

        Transfer t1 = restClient.postTransfer(acc1.id, acc2.id, new BigDecimal(100));
        Transfer t2 = restClient.postTransfer(acc1.id, acc2.id, new BigDecimal(100));
        Transfer t3 = restClient.postTransfer(acc1.id, acc2.id, new BigDecimal(100));
        Transfer t4 = restClient.postTransfer(acc2.id, acc1.id, new BigDecimal(100));

        Collection<Transfer> transfers = restClient.getTransfersByAccountId(acc1.id);

        assertEquals(5, transfers.size()); // we expect 5, as initial balance counts as a transfer
        assertTrue(transfers.stream().anyMatch(a -> a.equals(t1)));
        assertTrue(transfers.stream().anyMatch(a -> a.equals(t2)));
        assertTrue(transfers.stream().anyMatch(a -> a.equals(t3)));
        assertTrue(transfers.stream().anyMatch(a -> a.equals(t4)));
    }

    @Test
    public void getAccountTransfersAccountDoesntExistReturns404() throws IOException {
        String id = "doNotExist";

        HttpUriRequest request = new HttpGet(url + id + "/transfers");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusLine().getStatusCode());
    }
}
