package ui;

import chess.ChessGame;
import websocket.messages.LoadGameMessage;

public class ServerMessageManager {

    private final ui.ChessBoard boardDrawer;

    public ServerMessageManager() {
        boardDrawer = new ChessBoard();
    }

    public void loadGame(LoadGameMessage message) {
        ChessGame game = message.getGame();
        chess.ChessBoard board = game.getBoard();
        System.out.print("\n");
        boardDrawer.drawGame(board, message.isWhite());
    }


}
