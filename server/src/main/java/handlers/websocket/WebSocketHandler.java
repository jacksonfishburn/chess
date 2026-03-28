package handlers.websocket;

import chess.ChessGame;
import dataaccess.GameDAO;
import io.javalin.websocket.*;
import json.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final static ConnectionManager connections = new ConnectionManager();
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
            case CONNECT -> connect(command, ctx.session);
            case MAKE_MOVE -> makeMove(command);
//            case LEAVE -> ;
//            case RESIGN -> ;
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(UserGameCommand command, Session session) throws Exception {
        int gameID = command.getGameID();
        ChessGame game = gameDAO.getGame(gameID).game();

        connections.add(session, gameID);

        ServerMessage gameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        ServerMessage playerJoinedMessage = new ServerMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                command.getBroadcastMessage()
        );

        connections.sendTo(session, gameID, gameMessage);
        connections.broadcast(session, gameID, playerJoinedMessage);
    }

    private void makeMove(UserGameCommand command) throws Exception {
        ChessGame game = gameDAO.getGame(command.getGameID()).game();

        game.makeMove(command.getMove());

        ServerMessage gameUpdateMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, game);
        ServerMessage moveMadeMessage = new ServerMessage(
                ServerMessage.ServerMessageType.NOTIFICATION,
                command.getBroadcastMessage()
        );

        connections.broadcast(null, command.getGameID(), gameUpdateMessage);
        connections.broadcast(null, command.getGameID(), moveMadeMessage);
    }
}
