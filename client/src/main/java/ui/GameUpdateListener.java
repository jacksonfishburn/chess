package ui;

import chess.ChessGame;

public interface GameUpdateListener {
    void onGameUpdated(ChessGame game);
}
