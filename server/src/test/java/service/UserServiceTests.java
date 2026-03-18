package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import models.LoginRequest;
import models.SessionStartResult;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserServiceTests {

    UserData userData;
    UserService service;

    @BeforeEach
    public void setup() {
        service = new UserService(new MemoryUserDAO(), new MemoryAuthDAO());
        userData = new UserData("ChessPlayer", "chessPassW0rd", "epicchess@email.com");
    }

    @Test
    public void positiveRegisterTest() throws Exception {
        SessionStartResult result;
        String expectedUsername = "ChessPlayer";

        result = service.register(userData);

        Assertions.assertEquals(expectedUsername, result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void negativeRegisterTest() {
        userData = new UserData("ChessPlayer", null, "epicchess@email.com");

        Assertions.assertThrows(BadRequestException.class, () -> {
            service.register(userData);
        });
    }

    @Test
    public void positiveLoginTest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("ChessPlayer", "chessPassW0rd");
        SessionStartResult result;
        String expectedUsername = "ChessPlayer";

        service.register(userData);
        result = service.login(loginRequest);

        Assertions.assertEquals(expectedUsername, result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void negativeLoginTest() {
        LoginRequest loginRequest = new LoginRequest("ChessPlayer", "chessPassW0rd");

        Assertions.assertThrows(UnauthorizedException.class, () -> {
            service.login(loginRequest);
        });
    }
}