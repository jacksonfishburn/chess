package ui;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ClientCommunicator {
    private static final int TIMEOUT_MILLIS = 5000;
    private final String url;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ClientCommunicator(String url) {
        this.url = url;
    }

    public String post(String message, String authToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .timeout(java.time.Duration.ofMillis(TIMEOUT_MILLIS))
                .header("authorization", authToken)
                .POST(HttpRequest.BodyPublishers.ofString(message, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if(httpResponse.statusCode() == 200) {
            return httpResponse.body();
        }
        else {
            throw new BadResponseException(httpResponse.body(), httpResponse.statusCode());
        }
    }
}