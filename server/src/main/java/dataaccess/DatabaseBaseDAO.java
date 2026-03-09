package dataaccess;

import exceptions.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseBaseDAO {
    private String[] createStatements;

    protected void setCreateStatements(String[] statements) {
        createStatements = statements;
    }

    protected void configureDatabase() throws Exception {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to get connection", e);
        }
    }

    public void clear() throws Exception {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "DROP TABLE IF EXISTS auth";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.executeUpdate();
                configureDatabase();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to get connection", e);
        }
    }
}
