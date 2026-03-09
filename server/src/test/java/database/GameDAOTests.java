package database;

import dataaccess.DatabaseGameDAO;
import exceptions.DataAccessException;
import models.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;


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

    @Test
    public void createInvalidGameTest() {
        Assertions.assertThrows(DataAccessException.class, () ->
                db.createGame(null, null)
        );
    }

    @Test
    public void getValidGameTest() throws Exception {
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

    @Test
    public void getNonexistentGameTest() throws Exception {
        GameData result = db.getGame(1000);
        Assertions.assertNull(result);
    }

    @Test
    public void listGamesTest() throws Exception {
        List<String> gameNames = List.of("name1", "name2", "name3");
        for (String gameName : gameNames) {
            db.createGame("username", gameName);
        }

        Collection<GameData> gameList = db.listGames();

        for (GameData game: gameList) {
            Assertions.assertTrue(gameNames.contains(game.gameName()));
        }
    }

    @Test
    public void emptyListTest() throws Exception {
        Collection<GameData> result = db.listGames();
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void updateGameTest() throws Exception {
        int id = db.createGame("username", "gameName");
        db.updateGame(id, "BLACK", "blackUser");
        db.updateGame(id, "WHITE", "whiteUser");

        GameData result = db.getGame(id);
        Assertions.assertEquals("blackUser", result.blackUserName());
        Assertions.assertEquals("whiteUser", result.whiteUserName());
    }

    @Test
    public void updateNonexistentGameTest() throws Exception {
        db.updateGame(1234, "BLACK", "username");
        Assertions.assertNull(db.getGame(1234));
    }

    @Test
    public void nameNotTakenTest() throws Exception {
        Assertions.assertFalse(db.isNameTaken("UnusedName"));
    }

    @Test
    public void nameTakenTest() throws Exception {
        db.createGame("username", "UsedName");

        Assertions.assertTrue(db.isNameTaken("UsedName"));
    }
}