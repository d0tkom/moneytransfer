import com.google.gson.Gson;
import db.DataStore;
import model.AccountResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RestApiTest {
    private final String jsonMimeType = "application/json";
    private final Gson gson = new Gson();

    private static DataStore db;

    @BeforeClass
    public static void startServer() {
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
        HttpUriRequest request = new HttpGet("http://localhost:4567/accounts");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void getAccountsReturnsJson() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:4567/accounts");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(jsonMimeType, ContentType.getOrDefault(response.getEntity()).getMimeType());
    }

    @Test
    public void postAccountsWithEmtpyBodyReturns201() throws IOException {
        HttpPost request = new HttpPost("http://localhost:4567/accounts");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_CREATED, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postAccountsWithStartingBalanceReturns201() throws IOException {
        StringEntity requestEntity = new StringEntity(
                "{\"balance\": 100}",
                ContentType.APPLICATION_JSON);
        HttpPost request = new HttpPost("http://localhost:4567/accounts");
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_CREATED,response.getStatusLine().getStatusCode());
    }

    @Test
    public void postAccountsWithEmtpyBodyReturnsAccountJson() throws IOException {
        AccountResponse account = postAccount();

        assertNotNull(account);
        assertNotNull(account.id);
        assertEquals(account.balance, BigDecimal.ZERO);
    }

    @Test
    public void postAccountsWithBalanceReturnsAccountJson() throws IOException {
        BigDecimal balance = new BigDecimal(100);

        AccountResponse account = postAccountWithBalance(balance);

        assertNotNull(account);
        assertNotNull(account.id);
        assertEquals(account.balance, balance);
    }

    @Test
    public void getAccountsByIdGetsBackSameAccount() throws IOException {
        AccountResponse createdAccount = postAccount();

        AccountResponse returnedAccont = getAccountById(createdAccount.id);

        assertNotNull(createdAccount);
        assertNotNull(returnedAccont);
        assertEquals(createdAccount, returnedAccont);
    }

    @Test
    public void getAccountsReturnsAllAccounts() throws IOException {
        AccountResponse acc1 = postAccount();
        AccountResponse acc2 = postAccount();
        AccountResponse acc3 = postAccount();

        Collection<AccountResponse> accounts = getAccounts();

        assertEquals(3, accounts.size());
        assertTrue(accounts.stream().anyMatch(a -> a.equals(acc1)));
        assertTrue(accounts.stream().anyMatch(a -> a.equals(acc2)));
        assertTrue(accounts.stream().anyMatch(a -> a.equals(acc3)));
    }






    private AccountResponse postAccount() throws IOException {
        HttpPost request = new HttpPost("http://localhost:4567/accounts");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        return gson.fromJson(responseString, AccountResponse.class);
    }

    private AccountResponse postAccountWithBalance(BigDecimal balance) throws IOException {
        HttpPost request = new HttpPost("http://localhost:4567/accounts");

        StringEntity requestEntity = new StringEntity(
                "{\"balance\": " + balance.toString() +"}",
                ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        return gson.fromJson(responseString, AccountResponse.class);
    }

    private Collection<AccountResponse> getAccounts() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:4567/accounts");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        AccountResponse[] arr = gson.fromJson(responseString, AccountResponse[].class);

        return Arrays.stream(arr).collect(Collectors.toList());
    }

    private AccountResponse getAccountById(String id) throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:4567/accounts/" + id);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        return gson.fromJson(responseString, AccountResponse.class);
    }
}
