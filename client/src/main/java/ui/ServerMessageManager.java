package ui;

import chess.ChessGame;
import websocket.messages.LoadGameMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class ServerMessageManager {

    private static CompletableFuture<ChessGame> gameFuture;

    public ServerMessageManager() {
    }

    public void loadGame(LoadGameMessage message) {
        gameFuture.complete(message.getGame());
    }

    public static ChessGame getGame() throws ExecutionException, InterruptedException {
        return gameFuture.get();
    }

    public static void resetGame() {
        gameFuture = new CompletableFuture<>();
    }
}
