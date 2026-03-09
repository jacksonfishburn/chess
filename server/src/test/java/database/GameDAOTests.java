package database;

import dataaccess.DatabaseGameDAO;
import models.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;


public class GameDAOTests {
    DatabaseGameDAO db;

    @BeforeEach
    public void setup() throws Exception {
        db = new DatabaseGameDAO();
        db.clear();
    }

    @Test
    public void createValidGameTest() throws Exception {
        String username = "TestName";
        String gameName = "TestGame";
        int id = db.createGame(username, gameName);

        GameData result = db.getGame(id);

        Assertions.assertEquals(gameName, result.gameName());
        Assertions.assertEquals(id, result.gameID());
        Assertions.assertNull(result.whiteUserName());
        Assertions.assertNull(result.blackUserName());
        Assertions.assertNotNull(result.game());
    }


}
