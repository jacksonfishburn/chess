package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import models.*;

import java.util.Objects;

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

        if (gameDAO.isNameTaken(request.gameName()) || request.gameName() == null) {
            throw new BadRequestException("");
        }

        int gameID = gameDAO.createGame(username, request.gameName());
        return new CreateGameResult(gameID);
    }

    public void joinGame(AuthData authData, JoinGameRequest request) throws Exception {
        String username = authenticate(authData);
        GameData game = gameDAO.getGame(request.gameID());

        if (game == null) {
            throw new BadRequestException("");
        }

        if (!Objects.equals(request.playerColor(), "BLACK") &&
                !Objects.equals(request.playerColor(), "WHITE")
        ) {
            throw new BadRequestException("");
        }

        if (Objects.equals(request.playerColor(), "BLACK") &&
                game.blackUserName() != null ||
                Objects.equals(request.playerColor(), "WHITE") &&
                        game.whiteUserName() != null
        ) {
            throw new AlreadyTakenException("");
        }

        gameDAO.updateGame(request.gameID(), request.playerColor(), username);
    }

    public ListGameResult listGames(AuthData authData) throws Exception {
        authenticate(authData);

        return new ListGameResult(gameDAO.listGames());
    }
}