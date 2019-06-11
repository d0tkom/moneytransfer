package server;

import db.DataStore;
import model.AccountResponse;
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

public class RestApiPostAccountTest {
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

    @Before
    public void clearDb() {
        db.transfers.clear();
        db.accounts.clear();
    }

    @Test
    public void postAccountWithEmtpyBodyReturns201() throws IOException {
        HttpPost request = new HttpPost(url);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_CREATED, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postAccountReturnsJson() throws IOException {
        HttpPost request = new HttpPost(url);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(jsonMimeType, ContentType.getOrDefault(response.getEntity()).getMimeType());
    }

    @Test
    public void postAccountWithStartingBalanceReturns201() throws IOException {
        StringEntity requestEntity = new StringEntity(
                "{\"balance\": 100}",
                ContentType.APPLICATION_JSON);
        HttpPost request = new HttpPost(url);
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_CREATED,response.getStatusLine().getStatusCode());
    }

    @Test
    public void postAccountWithIncorrectJsonReturns400() throws IOException {
        StringEntity requestEntity = new StringEntity(
                "{",
                ContentType.APPLICATION_JSON);
        HttpPost request = new HttpPost(url);
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_BAD_REQUEST,response.getStatusLine().getStatusCode());
    }

    @Test
    public void postAccountWithEmtpyBodyReturnsAccountJson() throws IOException {
        AccountResponse account = restClient.postAccount();

        assertNotNull(account);
        assertNotNull(account.id);
        assertEquals(account.balance, BigDecimal.ZERO);
    }

    @Test
    public void postAccountWithBalanceReturnsAccountJson() throws IOException {
        BigDecimal balance = new BigDecimal(100);

        AccountResponse account = restClient.postAccount(balance);

        assertNotNull(account);
        assertNotNull(account.id);
        assertEquals(account.balance, balance);
    }
}
