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
        String response = get(accountsUrl + "/" + id);

        return gson.fromJson(response, AccountResponse.class);
    }

    public Collection<AccountResponse> getAccounts() throws IOException {
        String response = get(accountsUrl);

        AccountResponse[] arr = gson.fromJson(response, AccountResponse[].class);

        return Arrays.stream(arr).collect(Collectors.toList());
    }

    public AccountResponse postAccount() throws IOException {
        String response = post(accountsUrl);

        return gson.fromJson(response, AccountResponse.class);
    }

    public AccountResponse postAccount(BigDecimal balance) throws IOException {
        StringEntity requestEntity = new StringEntity(
                "{\"balance\": " + balance +"}",
                ContentType.APPLICATION_JSON);

        String response = post(accountsUrl, requestEntity);

        return gson.fromJson(response, AccountResponse.class);
    }

    public Transfer getTransferById(String id) throws IOException {
        String response = get(transfersUrl + "/" + id);

        return gson.fromJson(response, Transfer.class);
    }

    public Collection<Transfer> getTransfersByAccountId(String id) throws IOException {
        String response = get(accountsUrl + "/" + id + "/transfers");

        Transfer[] arr = gson.fromJson(response, Transfer[].class);

        return Arrays.stream(arr).collect(Collectors.toList());
    }

    public Collection<Transfer> getTransfers() throws IOException {
        String response = get(transfersUrl);

        Transfer[] arr = gson.fromJson(response, Transfer[].class);

        return Arrays.stream(arr).collect(Collectors.toList());
    }

    public Transfer postTransfer(String source, String target, BigDecimal amount) throws IOException {
        StringEntity requestEntity = new StringEntity(
                "{\"source\": " + source + ", \"target\": " + target + ", \"amount\": " + amount + "}",
                ContentType.APPLICATION_JSON);

        String response = post(transfersUrl, requestEntity);

        return gson.fromJson(response, Transfer.class);
    }

    private String get(String url) throws IOException {
        HttpUriRequest request = new HttpGet(url);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        return responseString;
    }

    private String post(String url) throws IOException {
        return post(url, null);
    }

    private String post(String url, StringEntity requestEntity) throws IOException {
        HttpPost request = new HttpPost(url);
        request.setEntity(requestEntity);

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        return responseString;
    }
}
