package handlers.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import exceptions.UnauthorizedException;
import io.javalin.websocket.*;
import json.JsonSerializer;
import models.GameData;
import org.jetbrains.annotations.NotNull;
import service.AuthService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final static ConnectionManager CONNECTIONS = new ConnectionManager();
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
    public void handleMessage(@NotNull WsMessageContext ctx) {
        UserGameCommand command = JsonSerializer.fromJson(ctx.message(), UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, ctx.session);
            case MAKE_MOVE -> makeMove(JsonSerializer.fromJson(ctx.message(), MakeMoveCommand.class), ctx.session);
            case LEAVE -> leave(command, ctx.session);
            case RESIGN -> resign(command, ctx.session);
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void connect(UserGameCommand command, Session session) {
        try {
            String username = authService.authorize(command.getAuthToken());

            int gameID = command.getGameID();
            GameData gameData = gameDAO.getGame(gameID);
            if (gameData == null) {
                sendErrorMessage(session, "Game not found");
                return;
            }

            CONNECTIONS.add(session, gameID);

            String notification = makeJoinNotification(username, gameData);

            LoadGameMessage gameMessage = new LoadGameMessage(gameData.game());
            NotificationMessage playerJoinedMessage = new NotificationMessage(notification);

            CONNECTIONS.sendTo(session, gameMessage);
            CONNECTIONS.broadcast(session, gameID, playerJoinedMessage);
        } catch (UnauthorizedException e) {
            sendErrorMessage(session, "Invalid auth token");
        } catch (Exception e) {
            sendErrorMessage(session, e.getMessage());
        }
    }

    private void makeMove(MakeMoveCommand command, Session session) {
        try {
            String username = authService.authorize(command.getAuthToken());
            GameData gameData = gameDAO.getGame(command.getGameID());
            if (gameData == null) {
                sendErrorMessage(session, "Game not found");
                return;
            }

            ChessGame game = gameData.game();
            ChessMove move = command.getMove();

            if (!validateMoveRequest(username, gameData, move, session)) {
                return;
            }

            String notification = makeMoveNotification(username, move, game.getBoard());
            game.makeMove(move);
            gameDAO.editGameState(command.getGameID(), game);

            LoadGameMessage gameUpdateMessage = new LoadGameMessage(game);
            NotificationMessage moveMadeMessage = new NotificationMessage(notification);
            NotificationMessage moveOutcomeMessage = getMoveOutcomeMessage(username, gameData);

            boolean gameEnded = game.isInCheckmate(ChessGame.TeamColor.WHITE)
                    || game.isInCheckmate(ChessGame.TeamColor.BLACK)
                    || game.isInStalemate(ChessGame.TeamColor.WHITE)
                    || game.isInStalemate(ChessGame.TeamColor.BLACK);
            if (gameEnded) {
                gameDAO.markGameOver(command.getGameID());
            }

            CONNECTIONS.broadcast(null, command.getGameID(), gameUpdateMessage);
            CONNECTIONS.broadcast(session, command.getGameID(), moveMadeMessage);

            if (moveOutcomeMessage != null) {
                CONNECTIONS.broadcast(null, command.getGameID(), moveOutcomeMessage);
            }

        } catch (UnauthorizedException e) {
            sendErrorMessage(session, "Invalid auth token");
        } catch (InvalidMoveException e) {
            sendErrorMessage(session, "Invalid move");
        } catch (Exception e) {
            sendErrorMessage(session, e.getMessage());
        }
    }

    private void leave(UserGameCommand command, Session session) {
        try {
            String username = authService.authorize(command.getAuthToken());
            GameData gameData = gameDAO.getGame(command.getGameID());
            if (gameData == null) {
                sendErrorMessage(session, "Game not found");
                return;
            }

            CONNECTIONS.remove(session, command.getGameID());
            ChessGame.TeamColor playerColor = getPlayerColor(username, gameData);
            if (playerColor != null) {
                gameDAO.updateGame(command.getGameID(), playerColor.name(), null);
            }

            String notification = String.format("%s left the game", username);
            NotificationMessage playerLeftMessage = new NotificationMessage(notification);
            CONNECTIONS.broadcast(null, command.getGameID(), playerLeftMessage);
        } catch (UnauthorizedException e) {
            sendErrorMessage(session, "Invalid auth token");
        } catch (Exception e) {
            sendErrorMessage(session, e.getMessage());
        }
    }

    private void resign(UserGameCommand command, Session session) {
        try {
            String username = authService.authorize(command.getAuthToken());
            GameData gameData = gameDAO.getGame(command.getGameID());
            if (gameData == null) {
                sendErrorMessage(session, "Game not found");
                return;
            }

            if (gameData.gameOver()) {
                sendErrorMessage(session, "Game is over");
                return;
            }

            if (getPlayerColor(username, gameData) == null) {
                sendErrorMessage(session, "Observers cannot resign");
                return;
            }

            gameDAO.markGameOver(command.getGameID());
            NotificationMessage resignedMessage = new NotificationMessage(username + " resigned. Game is over");
            CONNECTIONS.broadcast(null, command.getGameID(), resignedMessage);
        } catch (UnauthorizedException e) {
            sendErrorMessage(session, "Invalid auth token");
        } catch (Exception e) {
            sendErrorMessage(session, e.getMessage());
        }
    }

    private NotificationMessage getMoveOutcomeMessage(String username, GameData gameData) {
        ChessGame.TeamColor opponentColor = (getPlayerColor(username, gameData) == ChessGame.TeamColor.WHITE) ?
                ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
        NotificationMessage moveOutcomeMessage;
        ChessGame game = gameData.game();

        if (game.isInCheckmate(opponentColor)) {
            moveOutcomeMessage = new NotificationMessage("Checkmate. " + username + " wins");
        } else if (game.isInCheck(opponentColor)) {
            moveOutcomeMessage = new NotificationMessage("Check");
        } else if (game.isInStalemate(opponentColor)) {
            moveOutcomeMessage = new NotificationMessage("Stalemate. Draw.");
        } else {
            moveOutcomeMessage = null;
        }
         return moveOutcomeMessage;
    }


    private ChessGame.TeamColor getPlayerColor(String username, GameData gameData) {
        if (username.equals(gameData.whiteUserName())) {
            return ChessGame.TeamColor.WHITE;
        } else if (username.equals(gameData.blackUserName())) {
            return ChessGame.TeamColor.BLACK;
        } else {
            return null;
        }
    }

    private boolean validateMoveRequest(String username, GameData gameData, ChessMove move, Session session) {
        ChessGame game = gameData.game();
        ChessGame.TeamColor playerColor = getPlayerColor(username, gameData);

        if (playerColor == null) {
            sendErrorMessage(session, "Observers cannot make moves");
            return false;
        }

        if (gameData.gameOver()) {
            sendErrorMessage(session, "Game is over");
            return false;
        }

        if (game.getTeamTurn() != playerColor) {
            sendErrorMessage(session, "Not your turn");
            return false;
        }

        ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
        if (piece == null) {
            sendErrorMessage(session, "Invalid move");
            return false;
        }

        if (piece.getTeamColor() != playerColor) {
            sendErrorMessage(session, "You cannot move your opponent's piece");
            return false;
        }

        var validMoves = game.validMoves(move.getStartPosition());
        if (validMoves == null || !validMoves.contains(move)) {
            sendErrorMessage(session, "Invalid move");
            return false;
        }

        return true;
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

    private void sendErrorMessage(Session session, String message) {
        ErrorMessage errorMessage = new ErrorMessage("Error: " + message);
        CONNECTIONS.sendTo(session, errorMessage);
    }
}
