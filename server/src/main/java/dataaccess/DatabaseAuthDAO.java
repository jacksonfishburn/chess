package dataaccess;

import exceptions.DataAccessException;
import models.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class DatabaseAuthDAO implements AuthDAO {

    public DatabaseAuthDAO() throws Exception {
        configureDatabase();
    }

    @Override
    public String createAuth(String username) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            String token = generateToken();
            var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                ps.setString(2, token);

                ps.executeUpdate();
                return token;
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    @Override
    public void deleteAuth(String auth) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM auth WHERE authToken=?";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, auth);

                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    @Override
    public AuthData getAuth(String auth) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, authToken FROM auth WHERE authToken=?";

            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, auth);
                try (ResultSet rs = ps.executeQuery()) {
                        return makeAuthObj(rs);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    private AuthData makeAuthObj(ResultSet rs) throws Exception {
        if (!rs.next()) {
            return null;
        }
        String username = rs.getString("username");
        String authToken = rs.getString("authToken");
        return new AuthData(username, authToken);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auth (
              `username` varchar(256) NOT NULL,
              `authToken` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
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

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public void clear() throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "DROP TABLE IF EXISTS auth";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
                configureDatabase();
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }
}
