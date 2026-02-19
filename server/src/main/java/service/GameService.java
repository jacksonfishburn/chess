package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import models.CreateGameRequest;
import models.CreateGameResult;

public class GameService {
    private final GameDAO gameDAO;

    public GameService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    public CreateGameResult createGame(CreateGameRequest request) {
        int gameID = gameDAO.createGame(request.userName(), request.gameName());
        return new CreateGameResult(gameID);
    }

}
