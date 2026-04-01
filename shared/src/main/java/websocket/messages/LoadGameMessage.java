package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    private final ChessGame game;
    private final boolean isWhite;

    public LoadGameMessage(ChessGame game, boolean isWhite) {
        type = ServerMessageType.LOAD_GAME;
        this.isWhite = isWhite;
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }

    public boolean isWhite() {
        return isWhite;
    }
}
