import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
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
    public void getAccount() throws IOException {
        HttpUriRequest request = new HttpGet("http://localhost:4567/transfers");

        HttpResponse response = HttpClientBuilder.create().build().execute(request);
    }
}
