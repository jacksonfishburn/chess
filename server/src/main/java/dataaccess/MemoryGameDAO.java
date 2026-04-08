package dataaccess;

import chess.ChessGame;
import models.GameData;

import java.util.*;


public class MemoryGameDAO implements GameDAO {
    Map<Integer, GameData> data = new HashMap<>();

    int currentID = 1000;

    @Override
    public int createGame(String username, String gameName) {
        GameData game =  new GameData(currentID, null, null, gameName, new ChessGame(), false);
        data.put(currentID, game);
        return currentID++;
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

        if (Objects.equals(playerColor, "WHITE")) {
            newGame = new GameData(gameID, userName, game.blackUserName(), game.gameName(), game.game(), game.gameOver());
        } else {
            newGame = new GameData(gameID, game.whiteUserName(), userName, game.gameName(), game.game(), game.gameOver());
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

    @Override
    public void editGameState(int gameID, ChessGame game) {
        GameData currentGame = data.get(gameID);
        if (currentGame != null) {
            GameData updatedGame = new GameData(gameID, currentGame.whiteUserName(), currentGame.blackUserName(), currentGame.gameName(), game, currentGame.gameOver());
            data.put(gameID, updatedGame);
        }
    }

    @Override
    public void markGameOver(int gameID) {
        GameData currentGame = data.get(gameID);
        if (currentGame != null) {
            GameData updatedGame = new GameData(gameID, currentGame.whiteUserName(), currentGame.blackUserName(), currentGame.gameName(), currentGame.game(), true);
            data.put(gameID, updatedGame);
        }
    }
}
