package handlers.websocket;

import dataaccess.GameDAO;
import io.javalin.websocket.*;
import json.JsonSerializer;
import models.GameData;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final GameDAO gameDAO;

    public WebSocketHandler(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    @Override
    public void handleConnect(@NotNull WsConnectContext ctx) {
        ctx.enableAutomaticPings();
        System.out.println("Websocket connected");
    }

    @Override
    public void handleMessage(@NotNull WsMessageContext ctx) throws Exception {
        UserGameCommand command = JsonSerializer.fromJson(ctx.message(), UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), command.getGameID(), ctx.session);
//            case MAKE_MOVE -> ;
//            case LEAVE -> ;
//            case RESIGN -> ;
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(String authToken, int gameID, Session session) throws Exception {
        connections.add(session, gameID);
        GameData game = gameDAO.getGame(gameID);
        ServerMessage gameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game.game());
        ServerMessage playerJoinedMessage = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);

        connections.sendTo(session, gameID, gameMessage);
        connections.broadcast(session, gameID, playerJoinedMessage);
    }
}
