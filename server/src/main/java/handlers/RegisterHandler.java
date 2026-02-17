package handlers;

import service.RegisterService;
import models.UserData;

import com.google.gson.*;
import io.javalin.http.*;
import org.jetbrains.annotations.NotNull;

public class RegisterHandler implements Handler {
    @Override
    public void handle(@NotNull Context context) throws Exception {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        UserData data = gson.fromJson(context.body(), UserData.class);

        RegisterService service = new RegisterService();
    }
}
