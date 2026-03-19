package service;

import dataaccess.*;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ClearServiceTests {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    ClearService service;
    String testName;
    String authToken;
    int gameID;

    @BeforeEach
    public void setup() throws Exception {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        service = new ClearService(userDAO, authDAO, gameDAO);

        testName = "TestName";
        UserData userData = new UserData(testName, "password", "email");

        userDAO.createUser(userData);
        authToken = authDAO.createAuth(testName);
        gameID = gameDAO.createGame(testName, "testGame");
    }

    @Test
    public void clearTest() throws Exception {
        service.clear();

        Assertions.assertNull(userDAO.getUser(testName));
        Assertions.assertNull(authDAO.getAuth(authToken));
        Assertions.assertEquals(0, gameDAO.listGames().size());
    }
}
