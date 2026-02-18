package handlers;

import dataaccess.AuthDAO;
import exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.AuthRequest;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.LogoutService;

public class LogoutHandler implements Handler {
    private static final Logger log = LoggerFactory.getLogger(LogoutHandler.class);
    private final AuthDAO authDAO;

    public LogoutHandler(AuthDAO authDAO) {
        this.authDAO = authDAO;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        LogoutService logoutService = new LogoutService(authDAO);
        try {
            AuthRequest authRequest = new AuthRequest(context.header("Authorization"));
            logoutService.logout(authRequest);
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