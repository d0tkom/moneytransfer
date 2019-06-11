package server;

import db.DataStore;
import model.AccountResponse;
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

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RestApiGetAccountByIdTest {
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

    @Before
    public void clearDb() {
        db.transfers.clear();
        db.accounts.clear();
    }

    @Test
    public void getAccountByIdReturns200() throws IOException {
        AccountResponse account = restClient.postAccount();

        HttpUriRequest request = new HttpGet(url + account.id);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getAccountByIdReturnsJson() throws IOException {
        AccountResponse account = restClient.postAccount();

        HttpUriRequest request = new HttpGet(url + account.id);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(jsonMimeType, ContentType.getOrDefault(response.getEntity()).getMimeType());
    }

    @Test
    public void getAccountByIdGetsBackSameAccount() throws IOException {
        AccountResponse createdAccount = restClient.postAccount();

        AccountResponse returnedAccont = restClient.getAccountById(createdAccount.id);

        assertNotNull(createdAccount);
        assertNotNull(returnedAccont);
        assertEquals(createdAccount, returnedAccont);
    }

    @Test
    public void getAccountByIdAccountDoesntExistReturns404() throws IOException {
        String id = "doNotExist";

        HttpUriRequest request = new HttpGet(url + id);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_NOT_FOUND, response.getStatusLine().getStatusCode());
    }
}
