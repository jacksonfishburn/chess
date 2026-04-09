package ui;

import chess.ChessGame;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

public class ServerMessageManager {

    private static final AtomicReference<ChessGame> game = new AtomicReference<>();
    private static final List<GameUpdateListener> listeners = new CopyOnWriteArrayList<>();

    public ServerMessageManager() {
    }

    public void loadGame(LoadGameMessage message) {
        game.set(message.getGame());
        for (var listener : listeners) {
            listener.onGameUpdated(message.getGame());
        }
    }

    public void notify(NotificationMessage message) {
        System.out.print("\nNotification: ");
        System.out.println(message.getMessage());
        System.out.print("\n-> ");
    }

    public void displayError(ErrorMessage message) {
        System.out.print("\n");
        System.out.println(message.getErrorMessage());
        System.out.print("\n-> ");
    }

    public static ChessGame getGame(long timeoutMs) throws TimeoutException, InterruptedException {
        long deadline = System.currentTimeMillis() + timeoutMs;
        ChessGame currentGame = game.get();

        while (currentGame == null) {
            if (System.currentTimeMillis() >= deadline) {
                throw new TimeoutException("Timed out waiting for game state");
            }
            Thread.sleep(50);
            currentGame = game.get();
        }

        return currentGame;
    }

    public static void addGameUpdateListener(GameUpdateListener listener) {
        listeners.add(listener);
    }

    public static void removeGameUpdateListener(GameUpdateListener listener) {
        listeners.remove(listener);
    }

    public static void clearGame() {
        game.set(null);
    }


}
