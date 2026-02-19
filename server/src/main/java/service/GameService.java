package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import models.AuthData;
import models.CreateGameRequest;
import models.CreateGameResult;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(AuthData authData, CreateGameRequest request) throws Exception {
        AuthService authService = new AuthService(authDAO);
        String userName = authService.authenticate(authData);

        int gameID = gameDAO.createGame(userName, request.gameName());
        return new CreateGameResult(gameID);
    }
}
