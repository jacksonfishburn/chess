package handlers.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import io.javalin.websocket.*;
import json.JsonSerializer;
import models.GameData;
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

    public WebSocketHandler(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
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

        int gameID = command.getGameID();
        GameData gameData = gameDAO.getGame(gameID);

        connections.add(session, gameID);

        String notification = makeJoinNotification(username, gameData);

        LoadGameMessage gameMessage = new LoadGameMessage(gameData.game());
        NotificationMessage playerJoinedMessage = new NotificationMessage(notification);

        connections.sendTo(session, gameID, gameMessage);
        connections.broadcast(session, gameID, playerJoinedMessage);
    }

    private void makeMove(String jsonCommand) throws Exception {
        MakeMoveCommand command = JsonSerializer.fromJson(jsonCommand, MakeMoveCommand.class);
        String username = authService.authorize(command.getAuthToken());
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame game = gameData.game();
        ChessMove move = command.getMove();

        String notification = makeMoveNotification(username, move, game.getBoard());

        game.makeMove(move);

        LoadGameMessage gameUpdateMessage = new LoadGameMessage(game);
        NotificationMessage moveMadeMessage = new NotificationMessage(notification);

        connections.broadcast(null, command.getGameID(), gameUpdateMessage);
        connections.broadcast(null, command.getGameID(), moveMadeMessage);
    }

    private String makeJoinNotification(String username, GameData game) {
        if (username.equals(game.whiteUserName())) {
            return String.format("%s joined as white", username);
        } else if (username.equals(game.blackUserName())) {
            return String.format("%s joined as black", username);
        } else {
            return String.format("%s joined as observer", username);
        }
    }

    private String makeMoveNotification(String username, ChessMove move, chess.ChessBoard board) {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        ChessPiece movedPiece = board.getPiece(startPosition);
        String pieceType = movedPiece.getPieceType().name().toLowerCase();
        pieceType = Character.toUpperCase(pieceType.charAt(0)) + pieceType.substring(1);

        String startPos = String.format("%c%d", 'a' + startPosition.getColumn() - 1, startPosition.getRow());
        String endPos = String.format("%c%d", 'a' + endPosition.getColumn() - 1, endPosition.getRow());

        return String.format("%s moved their %s from %s to %s", username, pieceType, startPos, endPos);
    }
}
