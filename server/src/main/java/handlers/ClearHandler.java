package handlers;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.ErrorResponse;
import org.jetbrains.annotations.NotNull;
import service.ClearService;

public class ClearHandler implements Handler {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ClearHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    @Override
    public void handle(@NotNull Context context) {
        ClearService clearService = new ClearService(userDAO, authDAO, gameDAO);
        try {
            clearService.clear();
            context.status(200);
        } catch (Exception e) {
            context.json(new ErrorResponse(e.getMessage()));
            context.status(500);
        }
    }
}
