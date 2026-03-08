package database;

import dataaccess.DatabaseGameDAO;
import org.junit.jupiter.api.BeforeEach;

public class GameDAOTests {
    DatabaseGameDAO db;

    @BeforeEach
    public void setup() throws Exception {
        db = new DatabaseGameDAO();
        db.clear();
    }
}
