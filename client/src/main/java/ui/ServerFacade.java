package ui;

import chess.ChessMove;
import json.JsonSerializer;
import models.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

public class ServerFacade {

    private String authToken = "notnull";
    private final String url;

    private final ClientCommunicator communicator;
    private WebSocketCommunicator wsCommunicator;


    public ServerFacade(String url) {
        this.url = url;
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

    public void logout() throws Exception {
        communicator.delete("/session", authToken);
    }

    public void createGame(String gameName) throws Exception {
        CreateGameRequest request = new CreateGameRequest(gameName);
        String message = JsonSerializer.toJson(request);

        communicator.post("/game", message, authToken);
    }

    public ListGameResult listGames() throws Exception {
        String responseMessage = communicator.get("/game", authToken);
        return JsonSerializer.fromJson(responseMessage, ListGameResult.class);
    }

    public void joinGame(String playerColor, int gameID) throws Exception {
        JoinGameRequest request = new JoinGameRequest(playerColor, gameID);
        String message = JsonSerializer.toJson(request);

        communicator.put("/game", message, authToken);
    }

    public void connectWS(int gameID) {
        wsCommunicator = new WebSocketCommunicator(url);
        ServerMessageManager.resetGame();
        UserGameCommand command = new UserGameCommand(
                UserGameCommand.CommandType.CONNECT,
                authToken, gameID
        );
        wsCommunicator.sendCommand(command);
    }

    public void makeMove(int gameID, ChessMove move) {
        MakeMoveCommand command = new MakeMoveCommand(
                UserGameCommand.CommandType.MAKE_MOVE,
                authToken, gameID, move
        );
        wsCommunicator.sendCommand(command);
    }

    public void leaveGame(int gameID) {
        UserGameCommand command = new UserGameCommand(
                UserGameCommand.CommandType.LEAVE,
                authToken, gameID
        );
        wsCommunicator.sendCommand(command);
    }

    public void resign(int gameID) {
        UserGameCommand command = new UserGameCommand(
                UserGameCommand.CommandType.RESIGN,
                authToken, gameID
        );
        wsCommunicator.sendCommand(command);
    }

    public void clear() throws Exception {
        communicator.delete("/db", authToken);
    }
}
