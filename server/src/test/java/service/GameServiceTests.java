package service;

import dataaccess.*;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import models.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;

public class GameServiceTests {

    AuthDAO authDAO;
    GameDAO gameDAO;
    GameService service;
    AuthData authData;

    private CreateGameResult createGame(String name) throws Exception {
        CreateGameRequest createGameRequest = new CreateGameRequest(name);
        return service.createGame(authData, createGameRequest);
    }

    @BeforeEach
    public void setup() throws Exception {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        service = new GameService(authDAO, gameDAO);

        UserData userData  = new UserData("TestName", "Password", "testEmail@email.com");

        UserService userService = new UserService(new MemoryUserDAO(), authDAO);
        SessionStartResult registerResult =  userService.register(userData);

        authData = authDAO.getAuth(registerResult.authToken());
    }

    @Test
    public void positiveCreateGameTest() throws Exception{
        int expectedGameID = 1000;
        CreateGameResult result = createGame("GameName");

        Assertions.assertEquals(expectedGameID, result.gameID());
    }

    @Test
    public void negativeCreateGameTest() {
        Assertions.assertThrows(BadRequestException.class, () -> {
            createGame(null);
        });
    }

    @Test
    public void positiveJoinGameTest() throws Exception {
        int gameID = createGame("EvysCoolGame").gameID();
        String expectedUsername = "TestName";
        String expectedGameName = "EvysCoolGame";

        JoinGameRequest joinRequest = new JoinGameRequest("WHITE", gameID);
        service.joinGame(authData, joinRequest);

        GameData gameData = gameDAO.getGame(gameID);

        Assertions.assertEquals(expectedGameName, gameData.gameName());
        Assertions.assertEquals(expectedUsername, gameData.whiteUserName());
    }

    @Test
    public void negativeJoinGameTest() throws Exception {
        int gameID = createGame("gameName").gameID();
        JoinGameRequest joinRequest = new JoinGameRequest("BLUE", gameID);

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.joinGame(authData, joinRequest);
        });
    }

    @Test
    public void positiveListGamesTest() throws Exception {
        Collection<String> expectedNames = List.of("Game1", "Game2", "Game3");
        for (String gameName : expectedNames) {
            createGame(gameName);
        }

        Collection<GameInfo> actual = service.listGames(authData).games();

        for (GameInfo game : actual) {
            Assertions.assertTrue(expectedNames.contains(game.gameName()));
        }
    }

    @Test
    public void negativeListGamesTest() {
        AuthData nullAuthData = null;

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            service.listGames(nullAuthData);
        });
    }
}
