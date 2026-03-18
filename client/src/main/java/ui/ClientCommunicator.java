package ui;

import exceptions.BadResponseException;
import models.ErrorResponse;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class ClientCommunicator {
    private static final int TIMEOUT_MILLIS = 5000;
    private final String url;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    public ClientCommunicator(String url) {
        this.url = url;
    }

    public String get(String path, String authToken) throws Exception {
        HttpRequest request = createHttpBuilder(path, authToken)
                .GET()
                .build();

        return sendAndReturn(request);
    }

    public String post(String path, String message, String authToken) throws Exception {
        HttpRequest request = createHttpBuilder(path, authToken)
                .POST(HttpRequest.BodyPublishers.ofString(message, StandardCharsets.UTF_8))
                .build();

        return sendAndReturn(request);
    }

    public String put(String path, String message, String authToken) throws Exception {
        HttpRequest request = createHttpBuilder(path, authToken)
                .PUT(HttpRequest.BodyPublishers.ofString(message, StandardCharsets.UTF_8))
                .build();

        return sendAndReturn(request);
    }

    public void delete(String path, String authToken) throws Exception {
        HttpRequest request = createHttpBuilder(path, authToken)
                .DELETE()
                .build();

        sendAndReturn(request);
    }

    private HttpRequest.Builder createHttpBuilder(String path, String authToken) throws Exception {
        return HttpRequest.newBuilder()
                .uri(new URI(url + path))
                .timeout(Duration.ofMillis(TIMEOUT_MILLIS))
                .header("authorization", authToken);
    }

    private String sendAndReturn(HttpRequest request) throws Exception {
        HttpResponse<String> httpResponse = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if(httpResponse.statusCode() == 200) {
            return httpResponse.body();
        }
        else {
            throw new BadResponseException(
                    JsonSerializer.fromJson(httpResponse.body(), ErrorResponse.class).message(),
                    httpResponse.statusCode()
            );
        }
    }
}