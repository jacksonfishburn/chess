package handlers;

import dataaccess.AuthDAO;
import exceptions.BadRequestException;
import exceptions.UnauthorizedException;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import models.ErrorResponse;
import models.LoginRequest;
import org.jetbrains.annotations.NotNull;
import service.LoginService;

public class LoginHandler implements Handler {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public LoginHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    @Override
    public void handle(@NotNull Context context) {
        LoginService service = new LoginService(userDAO, authDAO);
        try {
            LoginRequest data = context.bodyAsClass(LoginRequest.class);
            context.json(service.login(data));
            context.status(200);
        } catch (BadRequestException e) {
            context.json(new ErrorResponse(e.getMessage()));
            context.status(400);
        } catch (UnauthorizedException e) {
            context.json(new ErrorResponse(e.getMessage()));
            context.status(401);
        } catch (Exception e) {
            context.json(new ErrorResponse(e.getMessage()));
            context.status(500);
        }
    }
}
