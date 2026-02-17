package handlers;

import dataaccess.AlreadyTakenException;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import models.ErrorResponse;
import models.RegisterResult;
import service.RegisterService;
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
    public void handle(@NotNull Context context) throws Exception {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        UserData data = gson.fromJson(context.body(), UserData.class);

        RegisterService service = new RegisterService(userDAO, authDAO);

        try {
            RegisterResult result = service.register(data);
            String jsonResult = gson.toJson(result);
            context.status(200);
            context.result(jsonResult);
        } catch (AlreadyTakenException e){
            context.status(403);
        }
    }
}
