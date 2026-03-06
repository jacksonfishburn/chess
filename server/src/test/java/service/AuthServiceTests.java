package service;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import exceptions.UnauthorizedException;
import models.AuthData;
import models.SessionStartResult;
import models.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class AuthServiceTests {
    AuthDAO authDAO;
    AuthService service;
    AuthData authData;

    @BeforeEach
    public void setup() throws Exception {
        authDAO = new MemoryAuthDAO();
        service = new AuthService(authDAO);

        UserData userData  = new UserData("TestName", "Password", "testEmail@email.com");

        UserService userService = new UserService(new MemoryUserDAO(), authDAO);
        SessionStartResult registerResult =  userService.register(userData);

        authData = authDAO.getAuth(registerResult.authToken());
    }

    @Test
    public void positiveAuthorizeTest() throws Exception{
        String expected = "TestName";
        String actual = service.authorize(authData.authToken());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void negativeAuthorizeTest() {
        String badAuth = UUID.randomUUID().toString();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            service.authorize(badAuth);
        });
    }

    @Test
    public void positiveLogoutTest() throws Exception {
        service.logout(authData.authToken());
        Assertions.assertNull(authDAO.getAuth(authData.authToken()));
    }

    @Test
    public void negativeLogoutTest() {
        String badAuth = UUID.randomUUID().toString();
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            service.logout(badAuth);
        });
    }
}
