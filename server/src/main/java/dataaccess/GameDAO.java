package dataaccess;

import models.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(String username, String gameName);
    GameData getGame(int gameID);
    Collection<GameData> listGames();
    void updateGame(int gameID, String playerColor, String userName);
}