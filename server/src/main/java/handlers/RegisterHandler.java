package handlers;

import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import service.RegisterService;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
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
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        RegisterService service = new RegisterService(userDAO, authDAO);

        String result;
        int status;

        try {
            UserData data = gson.fromJson(context.body(), UserData.class);
            result = gson.toJson(service.register(data));
            status = 200;
        } catch (AlreadyTakenException e){
            result = gson.toJson(new ErrorResponse(e.getMessage()));
            status = 403;
        } catch (JsonSyntaxException | BadRequestException e) {
            result = gson.toJson(new ErrorResponse("bad request"));
            status = 400;
        } catch (Exception e) {
            result = gson.toJson(new ErrorResponse(e.getMessage()));
            status = 500;
        }
        context.status(status);
        context.result(result);

    }
}
