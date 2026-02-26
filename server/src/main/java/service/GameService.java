package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import models.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class GameService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    private String authorize(AuthData authData) throws Exception {
        AuthService authService = new AuthService(authDAO);
        return authService.authorize(authData);
    }

    public CreateGameResult createGame(AuthData authData, CreateGameRequest request) throws Exception {
        String username = authorize(authData);

        if (gameDAO.isNameTaken(request.gameName()) || request.gameName() == null) {
            throw new BadRequestException("Game Name Invalid");
        }

        int gameID = gameDAO.createGame(username, request.gameName());
        return new CreateGameResult(gameID);
    }

    public void joinGame(AuthData authData, JoinGameRequest request) throws Exception {
        String username = authorize(authData);
        GameData game = gameDAO.getGame(request.gameID());

        if (game == null) {
            throw new BadRequestException("Game Not Found");
        }

        if (!Objects.equals(request.playerColor(), "BLACK") &&
                !Objects.equals(request.playerColor(), "WHITE")
        ) {
            throw new BadRequestException("Invalid Color");
        }

        if (Objects.equals(request.playerColor(), "BLACK") &&
                game.blackUserName() != null ||
                Objects.equals(request.playerColor(), "WHITE") &&
                        game.whiteUserName() != null
        ) {
            throw new AlreadyTakenException("Player Color Already Taken");
        }

        gameDAO.updateGame(request.gameID(), request.playerColor(), username);
    }

    public ListGameResult listGames(AuthData authData) throws Exception {
        authorize(authData);

        Collection<GameData> data = gameDAO.listGames();
        Collection<GameInfo> gameList = new ArrayList<>();

        for (GameData game : data) {
            GameInfo info = new GameInfo(
                    game.gameID(),
                    game.whiteUserName(),
                    game.blackUserName(),
                    game.gameName()
            );
            gameList.add(info);
        }
        return new ListGameResult(gameList);
    }
}