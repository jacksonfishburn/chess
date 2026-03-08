package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exceptions.DataAccessException;
import models.GameData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class DatabaseGameDAO implements GameDAO{

    int currentID = 1000;

    public DatabaseGameDAO() throws Exception {
        configureDatabase();
    }

    @Override
    public int createGame(String username, String gameName) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO games (gameID, gameName, game) VALUES (?, ?, ?)";
            String game = new Gson().toJson(new ChessGame());

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, ++currentID);
                ps.setString(2, gameName);
                ps.setString(3, game);

                ps.executeUpdate();
                return currentID;
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return List.of();
    }

    @Override
    public void updateGame(int gameID, String playerColor, String userName) {

    }

    @Override
    public boolean isNameTaken(String name) {
        return false;
    }

    @Override
    public void clear() throws Exception{
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "DROP TABLE IF EXISTS games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
                configureDatabase();
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              `gameID` int NOT NULL,
              `whiteUserName` varchar(256) NOT NULL,
              `blackUserName` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NOT NULL
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws Exception {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }
}
