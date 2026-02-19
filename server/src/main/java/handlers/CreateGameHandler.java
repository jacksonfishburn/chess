package handlers;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.CreateGameRequest;
import models.ErrorResponse;
import org.jetbrains.annotations.NotNull;
import service.GameService;

public class CreateGameHandler implements Handler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public CreateGameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    @Override
    public void handle(@NotNull Context context) throws Exception {
        GameService service = new GameService(gameDAO);
        String authToken = context.header("Authorization");

        try {
            String userName = authDAO.authorize(authToken);
            CreateGameRequest createRequest = new CreateGameRequest(userName, authToken);
            context.json(service.createGame(createRequest));
            context.status(200);
        } catch (UnauthorizedException e) {
            context.json(new ErrorResponse(e.getMessage()));
            context.status(401);
        }
    }
}