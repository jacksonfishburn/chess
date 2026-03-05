package dataaccess;

import exceptions.DataAccessException;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class DatabaseUserDAO implements UserDAO{

    public DatabaseUserDAO() throws Exception {
        configureDatabase();
    }

    @Override
    public void createUser(UserData data) throws Exception {
        String hashPassword = BCrypt.hashpw(data.password(), BCrypt.gensalt());
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, data.username());
                ps.setString(2, hashPassword);
                ps.setString(3, data.email());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    @Override
    public UserData getUser(String username) throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, email, password FROM users WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (ResultSet rs = ps.executeQuery()) {
                    return makeUserObj(rs);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }

    private UserData makeUserObj(ResultSet rs) throws Exception {
        if (!rs.next()) {
            return null;
        }
        String username = rs.getString("username");
        String password = rs.getString("password");
        String email = rs.getString("email");
        return new UserData(username, password, email);
    }

    @Override
    public boolean verifyPassword(String username, String password) throws Exception {
        UserData user = getUser(username);
        String correctPassword = user.password();
        return BCrypt.checkpw(password, correctPassword);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  users (
              `id` int NOT NULL AUTO_INCREMENT,
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              PRIMARY KEY (`id`),
              INDEX (username)
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

    @Override
    public void clear() throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "DROP TABLE IF EXISTS users";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to get connection", e);
        }
    }
}
