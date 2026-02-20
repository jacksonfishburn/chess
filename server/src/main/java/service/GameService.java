package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import models.AuthData;
import models.CreateGameRequest;
import models.CreateGameResult;
import models.JoinGameRequest;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    private String authenticate(AuthData authData) throws Exception {
        AuthService authService = new AuthService(authDAO);
        return authService.authenticate(authData);
    }

    public CreateGameResult createGame(AuthData authData, CreateGameRequest request) throws Exception {
        String username = authenticate(authData);

        int gameID = gameDAO.createGame(username, request.gameName());
        return new CreateGameResult(gameID);
    }

    public void joinGame(AuthData authData, JoinGameRequest request) throws Exception {
        String username = authenticate(authData);

        gameDAO.updateGame(request.gameID(), request.playerColor(), username);
    }
}
