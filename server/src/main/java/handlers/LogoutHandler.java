package handlers;

import dataaccess.AuthDAO;
import exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.LogoutService;

public class LogoutHandler implements Handler {
    private final AuthDAO authDAO;

    public LogoutHandler(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        LogoutService logoutService = new LogoutService(authDAO);
        String authToken = context.header("Authorization");
        try {
            authDAO.authorize(authToken);
            logoutService.logout(authToken);
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