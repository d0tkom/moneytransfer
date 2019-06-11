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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.RestClient;

import java.io.IOException;
import java.math.BigDecimal;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RestApiGetTransferByIdTest {
    private final String jsonMimeType = "application/json";
    private final String url = "http://localhost:4567/transfers/";

    private static RestClient restClient;
    private static DataStore db;

    private AccountResponse acc1;
    private AccountResponse acc2;

    @BeforeClass
    public static void startServer() {
        restClient = new RestClient();

        db = new DataStore();
        RestApi api = new RestApi(db);

        api.listen();
    }

    @Before
    public void setUp() throws IOException {
        db.transfers.clear();
        db.accounts.clear();

        acc1 = restClient.postAccount();
        acc2 = restClient.postAccount(new BigDecimal(100));
    }

    @Test
    public void getTransferByIdReturns200() throws IOException {
        Transfer transfer = restClient.postTransfer(acc2.id, acc1.id, new BigDecimal(100));

        HttpUriRequest request = new HttpGet(url + transfer.id);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getTransferByIdReturnsJson() throws IOException {
        Transfer transfer = restClient.postTransfer(acc2.id, acc1.id, new BigDecimal(100));

        HttpUriRequest request = new HttpGet(url + transfer.id);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(jsonMimeType, ContentType.getOrDefault(response.getEntity()).getMimeType());
    }

    @Test
    public void getTransferByIdGetsBackSameTransfer() throws IOException {
        Transfer createdTransfer = restClient.postTransfer(acc2.id, acc1.id, new BigDecimal(100));

        Transfer returnedTransfer = restClient.getTransferById(createdTransfer.id);

        assertNotNull(createdTransfer);
        assertNotNull(returnedTransfer);
        assertEquals(createdTransfer, returnedTransfer);
    }

    @Test
    public void getTransferByIdTransferDoesntExistReturns404() throws IOException {
        String id = "doNotExist";

        HttpUriRequest request = new HttpGet(url + id);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusLine().getStatusCode());
    }
}
