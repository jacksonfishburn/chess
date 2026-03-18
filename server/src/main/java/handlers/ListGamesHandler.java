package handlers;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.ErrorResponse;
import models.ListGameResult;
import org.jetbrains.annotations.NotNull;
import service.GameService;

public class ListGamesHandler implements Handler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public ListGamesHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    @Override
    public void handle(@NotNull Context context) {
        String authToken = context.header("Authorization");

        GameService service = new GameService(authDAO, gameDAO);

        try {
            ListGameResult result = service.listGames(authToken);
            context.json(result);
            context.status(200);
        } catch (UnauthorizedException e) {
            context.json(new ErrorResponse("Error: Not Authorized"));
            context.status(401);
        } catch (Exception e) {
            context.json(new ErrorResponse(String.format("Error: %s", e.getMessage())));
            context.status(500);
        }
    }
}
