package dataaccess;

import chess.ChessGame;
import exceptions.DataAccessException;
import json.JsonSerializer;
import models.GameData;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class DatabaseGameDAO extends DatabaseBaseDAO implements GameDAO{

    int currentID = 1000;

    public DatabaseGameDAO() throws Exception {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (
              `gameID` int NOT NULL,
              `whiteUserName` varchar(256) DEFAULT NULL,
              `blackUserName` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              `gameOver` boolean NOT NULL DEFAULT false,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        setCreateStatements(createStatements);
        configureDatabase();
    }

    @Override
    public int createGame(String username, String gameName) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO games (gameID, gameName, game, gameOver) VALUES (?, ?, ?, ?)";
            String game = JsonSerializer.toJson(new ChessGame());

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, ++currentID);
                ps.setString(2, gameName);
                ps.setString(3, game);
                ps.setBoolean(4, false);

                ps.executeUpdate();
                return currentID;
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    @Override
    public GameData getGame(int gameID) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game, gameOver FROM games WHERE gameID=?";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);

                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    } return makeGameData(rs);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    @NotNull
    private GameData makeGameData(@NotNull ResultSet rs) throws Exception {
        int gameID = rs.getInt("gameID");
        String whiteUserName = rs.getString("whiteUsername");
        String blackUserName = rs.getString("blackUsername");
        String gameName = rs.getString("gameName");
        ChessGame game = JsonSerializer.fromJson(rs.getString("game"), ChessGame.class);
        boolean gameOver = rs.getBoolean("gameOver");

        return new GameData(gameID, whiteUserName, blackUserName, gameName, game, gameOver);
    }

    @Override
    public Collection<GameData> listGames() throws Exception {
        Collection<GameData> gameList = new ArrayList<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game, gameOver FROM games";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        GameData game = makeGameData(rs);
                        gameList.add(game);
                    } return gameList;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    @Override
    public void updateGame(int gameID, String playerColor, String userName) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE games SET blackUserName=? WHERE gameID=?";
                if (Objects.equals(playerColor, "WHITE")) {
                statement = "UPDATE games SET whiteUserName=? WHERE gameID=?";
            }
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, userName);
                ps.setInt(2, gameID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    @Override
    public boolean isNameTaken(String name) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameName FROM games WHERE gameName=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, name);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    @Override
    public void clear() throws Exception{
        String statement = "DROP TABLE IF EXISTS games";
        clearTable(statement);
    }

    @Override
    public void editGameState(int gameID, ChessGame game) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE games SET game=? WHERE gameID=?";
            String gameJson = JsonSerializer.toJson(game);

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, gameJson);
                ps.setInt(2, gameID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    @Override
    public void markGameOver(int gameID) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE games SET gameOver=? WHERE gameID=?";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setBoolean(1, true);
                ps.setInt(2, gameID);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }
}
