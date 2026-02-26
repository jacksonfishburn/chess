package handlers;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.AuthData;
import models.ErrorResponse;
import models.JoinGameRequest;
import org.jetbrains.annotations.NotNull;
import service.GameService;

public class JoinGameHandler implements Handler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public JoinGameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    @Override
    public void handle(@NotNull Context context) {
        String authToken = context.header("Authorization");
        AuthData authData = authDAO.getAuth(authToken);

        GameService service = new GameService(authDAO, gameDAO);

        try {
            JoinGameRequest request = context.bodyAsClass(JoinGameRequest.class);
            service.joinGame(authData, request);
            context.status(200);
        } catch (UnauthorizedException e) {
            context.json(new ErrorResponse("Error: Not Authorized"));
            context.status(401);
        } catch (BadRequestException e) {
            context.json(new ErrorResponse("Error: Bad Request"));
            context.status(400);
        } catch (AlreadyTakenException e) {
            context.json(new ErrorResponse("Error: Already Taken"));
            context.status(403);
        } catch (Exception e) {
            context.json(new ErrorResponse(e.getMessage()));
            context.status(500);
        }
    }
}
