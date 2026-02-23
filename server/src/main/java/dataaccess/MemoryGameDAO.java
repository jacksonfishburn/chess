package dataaccess;

import chess.ChessGame;
import models.GameData;
import models.GameInfo;

import java.util.*;


public class MemoryGameDAO implements GameDAO {
    Map<Integer, GameData> data = new HashMap<>();

    int currentID = 1000;

    @Override
    public int createGame(String username, String gameName) {
        GameData game =  new GameData(currentID, username, null, gameName, new ChessGame());
        data.put(currentID, game);
        return currentID++;
    }

    @Override
    public GameData getGame(int gameID) {
        return data.get(gameID);
    }

    @Override
    public Collection<GameInfo> listGames() {
        Collection<GameInfo> gameList = new ArrayList<>();
        for (GameData game : data.values()) {
            GameInfo info = new GameInfo(
                game.gameID(),
                game.whiteUserName(),
                game.blackUserName(),
                game.gameName()
            );

                gameList.add(info);
        }
        return gameList;
    }

    @Override
    public void updateGame(int gameID, String playerColor, String userName) {
        GameData game = data.get(gameID);
        GameData newGame;

        if (Objects.equals(playerColor, "WHITE")) {
            newGame = new GameData(gameID, userName, game.blackUserName(), game.gameName(), game.game());
        } else {
            newGame = new GameData(gameID, game.whiteUserName(), userName, game.gameName(), game.game());
        }
        data.put(gameID, newGame);
    }

    public boolean isNameTaken(String name) {
        for (GameData game : data.values()) {
            if (Objects.equals(game.gameName(), name)) {
                return true;
            }
        } return false;
    }

    @Override
    public void clear() {
        data = new HashMap<>();
    }

}
