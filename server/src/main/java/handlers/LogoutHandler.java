package handlers;

import dataaccess.AuthDAO;
import exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.AuthData;
import org.jetbrains.annotations.NotNull;
import service.AuthService;

public class LogoutHandler implements Handler {
    private final AuthDAO authDAO;

    public LogoutHandler(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    @Override
    public void handle(@NotNull Context context) {
        String authToken = context.header("Authorization");
        AuthData authData = authDAO.getAuth(authToken);

        AuthService logoutService = new AuthService(authDAO);

        try {
            logoutService.logout(authData);
            context.status(200);
        } catch (UnauthorizedException e) {
            context.status(401);
            context.result(String.format("{ \"message\": \"Error: %s\" }", e.getMessage()));
        } catch (Exception e) {
            context.status(500);
            context.result(String.format("{ \"message\": \"Error: %s\" }", e.getMessage()));
        }
    }
}