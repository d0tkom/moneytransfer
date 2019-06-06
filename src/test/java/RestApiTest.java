import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class RestApiTest {

    @BeforeClass
    public static void setUp() {
        Main.main(new String []{});
    }

    @Test
    public void getAccountsReturns200() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:4567/accounts");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void postAccountsReturns201() throws IOException {
        HttpPost request = new HttpPost("http://localhost:4567/accounts");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);

        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_CREATED);
    }

}
