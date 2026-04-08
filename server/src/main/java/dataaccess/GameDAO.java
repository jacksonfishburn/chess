package dataaccess;

import chess.ChessGame;
import models.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(String username, String gameName) throws Exception;
    GameData getGame(int gameID) throws Exception;
    Collection<GameData> listGames() throws Exception;
    void updateGame(int gameID, String playerColor, String userName) throws Exception;
    boolean isNameTaken(String name) throws Exception;
    void editGameState(int gameID, ChessGame game) throws Exception;
    void clear() throws Exception;
}