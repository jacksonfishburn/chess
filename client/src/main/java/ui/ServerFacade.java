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

        return startSession("/session", JsonSerializer.toJson(request));
    }

    public SessionStartResult register(String username, String password, String email) throws Exception {
        UserData request = new UserData(username, password, email);

        return startSession("/user", JsonSerializer.toJson(request));
    }

    private SessionStartResult startSession(String path, String message) throws Exception {
        String responseMessage = communicator.post(path, message, "");

        SessionStartResult result = JsonSerializer.fromJson(responseMessage, SessionStartResult.class);
        authToken = result.authToken();
        return result;
    }

    public void logout() {

    }

    public CreateGameResult createGame(String gameName) throws Exception {
        CreateGameRequest request = new CreateGameRequest(gameName);
        String message = JsonSerializer.toJson(request);

        String responseMessage = communicator.post("/game", message, authToken);
        return JsonSerializer.fromJson(responseMessage, CreateGameResult.class);
    }

    public void joinGame(String playerColor, int gameID) {

    }

    public ListGameResult listGames() {
        return null;
    }

}
