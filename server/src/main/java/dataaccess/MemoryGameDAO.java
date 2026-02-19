package dataaccess;

import chess.ChessGame;
import models.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MemoryGameDAO implements GameDAO {
    Map<Integer, GameData> data = new HashMap<>();

    int currentID = 0;

    @Override
    public void createGame(String username, String gameName) {
        GameData game =  new GameData(currentID, username, null, gameName, new ChessGame());
        data.put(currentID, game);
        currentID++;
    }

    @Override
    public GameData getGame(int gameID) {
        return data.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return data.values();
    }

    @Override
    public void updateGame(int gameID, String playerColor, String userName) {
        GameData game = data.get(gameID);
        GameData newGame;

        if (Objects.equals(playerColor, "white")) {
            newGame = new GameData(gameID, userName, game.blackUserName(), game.gameName(), game.game());
        } else {
            newGame = new GameData(gameID, game.whiteUserName(), userName, game.gameName(), game.game());
        }
        data.put(gameID, newGame);
    }
}
