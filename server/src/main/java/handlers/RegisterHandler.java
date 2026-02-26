package handlers;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import service.UserService;
import exceptions.AlreadyTakenException;
import exceptions.BadRequestException;
import models.ErrorResponse;
import models.UserData;

import com.google.gson.*;
import io.javalin.http.*;
import org.jetbrains.annotations.NotNull;

public class RegisterHandler implements Handler {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public RegisterHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    @Override
    public void handle(@NotNull Context context) {
        UserService service = new UserService(userDAO, authDAO);
        try {
            UserData data = context.bodyAsClass(UserData.class);
            context.json(service.register(data));
            context.status(200);
        } catch (AlreadyTakenException e){
            context.json(new ErrorResponse("Error: Username already taken"));
            context.status(403);
        } catch (BadRequestException e) {
            context.json(new ErrorResponse("Error: Bad Request"));
            context.status(400);
        } catch (Exception e) {
            System.out.println("Exception type: " + e.getClass().getName());
            context.json(new ErrorResponse(e.getMessage()));
            context.status(500);
        }
    }
}
