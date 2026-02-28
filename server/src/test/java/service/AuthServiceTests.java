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
        String actual = service.authorize(authData);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void negativeAuthorizeTest() {
        AuthData nullAuth = null;
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            service.authorize(nullAuth);
        });
    }

    @Test
    public void positiveLogoutTest() throws Exception {
        service.logout(authData);
        Assertions.assertNull(authDAO.getAuth(authData.authToken()));
    }

    @Test
    public void negativeLogoutTest() {
        AuthData nullAuth = null;
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            service.logout(nullAuth);
        });
    }
}
