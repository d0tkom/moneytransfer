package server;

import db.DataStore;
import model.AccountResponse;
import model.Transfer;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import util.RestClient;

import java.io.IOException;
import java.math.BigDecimal;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RestApiPostTransferTest {
    private final String jsonMimeType = "application/json";
    private final String url = "http://localhost:4567/transfers";

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
    public void postTransferReturns201() throws IOException {
        HttpPost request = new HttpPost(url);

        StringEntity requestEntity = new StringEntity(
                "{\"source\": " + acc2.id + ", \"target\": " + acc1.id + ", \"amount\": " + 100 + "}",
                ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_CREATED, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postTransferReturnsTransferJson() throws IOException {
        Transfer transfer = restClient.postTransfer(acc2.id, acc1.id, new BigDecimal(100));

        assertNotNull(transfer);
        assertEquals(acc2.id, transfer.source);
        assertEquals(acc1.id, transfer.target);
        assertEquals(new BigDecimal(100), transfer.amount);
    }

    @Test
    public void postTransferNullAmountReturns400() throws IOException {
        HttpPost request = new HttpPost(url);

        StringEntity requestEntity = new StringEntity(
                "{\"source\": " + acc2.id + ", \"target\": " + acc1.id + ", \"amount\": " + null + "}",
                ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postTransferZeroAmountReturns400() throws IOException {
        HttpPost request = new HttpPost(url);

        StringEntity requestEntity = new StringEntity(
                "{\"source\": " + acc2.id + ", \"target\": " + acc1.id + ", \"amount\": " + 0 + "}",
                ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postTransferSameAccountReturns400() throws IOException {
        HttpPost request = new HttpPost(url);

        StringEntity requestEntity = new StringEntity(
                "{\"source\": " + acc2.id + ", \"target\": " + acc2.id + ", \"amount\": " + 100 + "}",
                ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postTransferWithIncorrectJsonReturns400() throws IOException {
        HttpPost request = new HttpPost(url);

        StringEntity requestEntity = new StringEntity("{", ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_BAD_REQUEST,response.getStatusLine().getStatusCode());
    }
}
