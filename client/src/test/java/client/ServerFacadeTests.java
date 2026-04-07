package client;

import exceptions.BadResponseException;
import models.*;
import org.junit.jupiter.api.*;
import server.Server;
import ui.ServerFacade;
import ui.ServerMessageManager;

import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        facade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void setup() throws Exception {
        facade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    private String username;
    private String password;

    private SessionStartResult registerUser() throws Exception {
        username = "TestName";
        password = "PassW0rd";
        return facade.register(username, password, "email");
    }


    @Test
    public void validLoginTest() throws Exception {
        registerUser();

        SessionStartResult result = facade.login(username, password);

        Assertions.assertEquals(username, result.username());
    }

    @Test
    public void invalidLoginTest() {
        Assertions.assertThrows(BadResponseException.class, () ->
                facade.login("invalidUser", "password")
        );
    }

    @Test
    public void validRegisterTest() throws Exception {
        SessionStartResult result = registerUser();
        Assertions.assertEquals(username, result.username());
    }

    @Test
    public void invalidRegisterTest() throws Exception {
        registerUser();
        Assertions.assertThrows(BadResponseException.class, () ->
                facade.register(username, "password", "email")
        );
    }

    @Test
    public void logoutTest() throws Exception {
        registerUser();
        Assertions.assertDoesNotThrow(() -> facade.logout());
    }

    @Test
    public void badLogoutTest() {
        Assertions.assertThrows(BadResponseException.class, () ->
                facade.logout()
        );
    }

    private String gameName;

    private void createGame(String gameName) throws Exception {
        this.gameName = gameName;
        facade.createGame(gameName);
    }

    @Test
    public void createValidGame() throws Exception {
        registerUser();
        createGame("newGame");
        List<GameInfo> games = (List<GameInfo>) facade.listGames().games();
        Assertions.assertEquals(gameName, games.getFirst().gameName());
    }

    @Test
    public void createInvalidGame() throws Exception {
        registerUser();
        createGame("TestGame");
        Assertions.assertThrows(BadResponseException.class, () ->
                facade.createGame(gameName)
        );
    }

    @Test
    public void listGamesTest() throws Exception {
        String game1 = "Game1";
        String game2 = "Game2";
        String game3 = "Game3";

        registerUser();

        createGame(game1);
        createGame(game2);
        createGame(game3);

        List<GameInfo> games = (List<GameInfo>) facade.listGames().games();

        Assertions.assertEquals(game1, games.getFirst().gameName());
        Assertions.assertEquals(game2, games.get(1).gameName());
        Assertions.assertEquals(game3, games.get(2).gameName());
    }

    @Test
    public void listGamesUnauthorized() {
        Assertions.assertThrows(BadResponseException.class, () ->
                facade.listGames()
        );
    }

    @Test
    public void joinValidGameTest() throws Exception {
        registerUser();
        createGame("newGame");
        List<GameInfo> games = (List<GameInfo>) facade.listGames().games();
        GameInfo game = games.getFirst();

        Assertions.assertDoesNotThrow(() -> facade.joinGame("WHITE", game.gameID()));

        List<GameInfo> updatedGames = (List<GameInfo>) facade.listGames().games();
        GameInfo updatedGame = updatedGames.getFirst();

        Assertions.assertEquals(username, updatedGame.whiteUsername());
    }

    @Test
    public void joinInvalidGameTest() throws Exception {
        registerUser();
        Assertions.assertThrows(BadResponseException.class, () ->
                facade.joinGame("BLACK", 1234)
        );
    }

    @Test
    public void testClear() throws Exception {
        registerUser();
        createGame("Game1");
        createGame("Game2");
        createGame("Game3");
        createGame("Game4");

        facade.clear();

        Assertions.assertThrows(BadResponseException.class, () ->
                facade.listGames()
        );

        Assertions.assertThrows(BadResponseException.class, () ->
                facade.login(username, password)
        );
    }
}
