package dataaccess;

import models.GameData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    Map<String, GameData> data = new HashMap<>();

    @Override
    public void createGame(String username, String gameName) {

    }

    @Override
    public GameData getGame(String gameID) {
        return null;
    }

    @Override
    public List<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(String gameID, String playerColor, String userName) {

    }
}
