package util;

import com.google.gson.Gson;
import model.AccountResponse;
import model.Transfer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

// Helper class to use inside rest api tests
public class RestClient {
    private final Gson gson;
    private final String baseUrl = "http://localhost:4567/";
    private final String accountsUrl = baseUrl + "accounts";
    private final String transfersUrl = baseUrl + "transfers";

    public RestClient() {
        this.gson = new Gson();
    }

    public AccountResponse getAccountById(String id) throws IOException {
        HttpUriRequest request = new HttpGet(accountsUrl + "/" + id);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        return gson.fromJson(responseString, AccountResponse.class);
    }

    public Collection<AccountResponse> getAccounts() throws IOException {
        HttpUriRequest request = new HttpGet(accountsUrl);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        AccountResponse[] arr = gson.fromJson(responseString, AccountResponse[].class);

        return Arrays.stream(arr).collect(Collectors.toList());
    }

    public AccountResponse postAccount() throws IOException {
        HttpPost request = new HttpPost(accountsUrl);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        return gson.fromJson(responseString, AccountResponse.class);
    }

    public AccountResponse postAccount(BigDecimal balance) throws IOException {
        HttpPost request = new HttpPost(accountsUrl);

        StringEntity requestEntity = new StringEntity(
                "{\"balance\": " + balance +"}",
                ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        return gson.fromJson(responseString, AccountResponse.class);
    }

    public Transfer getTransferById(String id) throws IOException {
        HttpUriRequest request = new HttpGet(transfersUrl + "/" + id);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        return gson.fromJson(responseString, Transfer.class);
    }

    public Collection<Transfer> getTransfersByAccountId(String id) throws IOException {
        HttpUriRequest request = new HttpGet(accountsUrl + "/" + id + "/transfers");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        Transfer[] arr = gson.fromJson(responseString, Transfer[].class);

        return Arrays.stream(arr).collect(Collectors.toList());
    }

    public Collection<Transfer> getTransfers() throws IOException {
        HttpUriRequest request = new HttpGet(transfersUrl);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        Transfer[] arr = gson.fromJson(responseString, Transfer[].class);

        return Arrays.stream(arr).collect(Collectors.toList());
    }

    public Transfer postTransfer(String source, String target, BigDecimal amount) throws IOException {
        HttpPost request = new HttpPost(transfersUrl);

        StringEntity requestEntity = new StringEntity(
                "{\"source\": " + source + ", \"target\": " + target + ", \"amount\": " + amount + "}",
                ContentType.APPLICATION_JSON);
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        return gson.fromJson(responseString, Transfer.class);
    }
}
