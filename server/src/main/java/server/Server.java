package server;

import dataaccess.*;
import handlers.*;

import io.javalin.*;
import io.javalin.json.JavalinGson;

public class Server {

    private final Javalin javalin;

    UserDAO userDAO = new MemoryUserDAO();
    AuthDAO authDAO = new MemoryAuthDAO();
    GameDAO gameDAO = new MemoryGameDAO();

    public Server() {
        javalin = Javalin.create(config -> {
            config.jsonMapper(new JavalinGson());
            config.staticFiles.add("web");
        });

        javalin.post("/user", new RegisterHandler(userDAO, authDAO));
        javalin.post("/session", new LoginHandler(userDAO, authDAO));
        javalin.delete("/session", new LogoutHandler(authDAO));

        javalin.post("/game", new CreateGameHandler(authDAO, gameDAO));
        javalin.put("/game", new JoinGameHandler(authDAO, gameDAO));
        javalin.get("/game", new ListGamesHandler(authDAO, gameDAO));

        javalin.delete("/db", new ClearHandler(userDAO, authDAO, gameDAO));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
