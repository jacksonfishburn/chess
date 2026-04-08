package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{

    private final ChessGame game;

    public LoadGameMessage(ChessGame game) {
        type = ServerMessageType.LOAD_GAME;
        this.game = game;
    }

    public ChessGame getGame() {
        return game;
    }
}
