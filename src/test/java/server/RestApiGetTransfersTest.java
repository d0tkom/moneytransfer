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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestApiGetTransfersTest {
    private final String jsonMimeType = "application/json";
    private final String url = "http://localhost:4567/transfers";

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
    public void getTransfersReturns200() throws IOException {
        HttpUriRequest request = new HttpGet(url);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getTransfersReturnsJson() throws IOException {
        HttpUriRequest request = new HttpGet(url);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(jsonMimeType, ContentType.getOrDefault(response.getEntity()).getMimeType());
    }
}
