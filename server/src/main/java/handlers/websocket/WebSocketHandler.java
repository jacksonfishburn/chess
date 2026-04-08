package handlers.websocket;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.websocket.*;
import json.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import service.AuthService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final static ConnectionManager connections = new ConnectionManager();
    private final AuthService authService;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public WebSocketHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        authService = new AuthService(authDAO);
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) {
        ctx.enableAutomaticPings();
        System.out.println("Websocket connected");
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        UserGameCommand command = JsonSerializer.fromJson(ctx.message(), UserGameCommand.class);
        String jsonCommand = ctx.message();
        switch (command.getCommandType()) {
            case CONNECT -> connect(jsonCommand, ctx.session);
            case MAKE_MOVE -> makeMove(jsonCommand);
//            case LEAVE -> ;
//            case RESIGN -> ;
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(String jsonCommand, Session session) throws Exception {
        UserGameCommand command = JsonSerializer.fromJson(jsonCommand, UserGameCommand.class);

        String username = authService.authorize(command.getAuthToken());
        String notification = String.format("%s joined the game.", username);

        int gameID = command.getGameID();
        ChessGame game = gameDAO.getGame(gameID).game();

        connections.add(session, gameID);

        LoadGameMessage gameMessage = new LoadGameMessage(game);
        NotificationMessage playerJoinedMessage = new NotificationMessage(notification);

        connections.sendTo(session, gameID, gameMessage);
        connections.broadcast(session, gameID, playerJoinedMessage);
    }

    private void makeMove(String jsonCommand) throws Exception {
        MakeMoveCommand command = JsonSerializer.fromJson(jsonCommand, MakeMoveCommand.class);

        ChessGame game = gameDAO.getGame(command.getGameID()).game();

        game.makeMove(command.getMove());

        LoadGameMessage gameUpdateMessage = new LoadGameMessage(game);
        NotificationMessage moveMadeMessage = new NotificationMessage("player made move");

        connections.broadcast(null, command.getGameID(), gameUpdateMessage);
        connections.broadcast(null, command.getGameID(), moveMadeMessage);
    }
}
