package handlers;

import dataaccess.AuthDAO;
import exceptions.UnauthorizedException;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.AuthData;
import models.ErrorResponse;
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

        AuthService service = new AuthService(authDAO);

        try {
            service.logout(authData);
            context.status(200);
        } catch (UnauthorizedException e) {
            context.json(new ErrorResponse("Error: Unauthorized"));
            context.status(401);
        } catch (Exception e) {
            context.json(new ErrorResponse(e.getMessage()));
            context.status(500);
        }
    }
}