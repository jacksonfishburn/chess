package dataaccess;

import models.GameData;

import java.util.List;

public interface GameDAO {
    void createGame(String username, String gameName);
    GameData getGame(String gameID);
    List<GameData> listGames();
    void updateGame(String gameID, String playerColor, String userName);
}