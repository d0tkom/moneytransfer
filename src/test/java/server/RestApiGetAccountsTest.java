package server;

import db.DataStore;
import model.AccountResponse;
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
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestApiGetAccountsTest {
    private final String jsonMimeType = "application/json";
    private final String url = "http://localhost:4567/accounts";

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
    public void getAccountsReturns200() throws IOException {
        HttpUriRequest request = new HttpGet(url);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getAccountsReturnsJson() throws IOException {
        HttpUriRequest request = new HttpGet(url);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(jsonMimeType, ContentType.getOrDefault(response.getEntity()).getMimeType());
    }

    @Test
    public void getAccountsReturnsEmptyArray() throws IOException {
        Collection<AccountResponse> accounts = restClient.getAccounts();

        assertEquals(0, accounts.size());
    }

    @Test
    public void getAccountsReturnsAllAccounts() throws IOException {
        AccountResponse acc1 = restClient.postAccount();
        AccountResponse acc2 = restClient.postAccount();
        AccountResponse acc3 = restClient.postAccount();

        Collection<AccountResponse> accounts = restClient.getAccounts();

        assertEquals(3, accounts.size());
        assertTrue(accounts.stream().anyMatch(a -> a.equals(acc1)));
        assertTrue(accounts.stream().anyMatch(a -> a.equals(acc2)));
        assertTrue(accounts.stream().anyMatch(a -> a.equals(acc3)));
    }
}
