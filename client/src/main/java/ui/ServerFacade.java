package ui;

import models.*;

public class ServerFacade {

    private String authToken;
    private final ClientCommunicator communicator;


    public ServerFacade(String url) {
        communicator = new ClientCommunicator(url);
    }

    public SessionStartResult login(String username, String password) throws Exception {
        LoginRequest request = new LoginRequest(username, password);
        String message = JsonSerializer.toJson(request);

        String responseMessage = communicator.post("/session", message, "");

        SessionStartResult result = JsonSerializer.fromJson(responseMessage, SessionStartResult.class);
        authToken = result.authToken();
        return result;
    }

    public SessionStartResult register(String username, String password, String email) throws Exception {
        UserData request = new UserData(username, password, email);
        String message = JsonSerializer.toJson(request);

        String responseMessage = communicator.post("/user", message, "");

        SessionStartResult result = JsonSerializer.fromJson(responseMessage, SessionStartResult.class);
        authToken = result.authToken();
        return result;
    }

    public void logout() {

    }

    public CreateGameResult createGame(String gameName) {
        return null;
    }

    public void joinGame(String playerColor, int gameID) {

    }

    public ListGameResult listGames() {
        return null;
    }

}
