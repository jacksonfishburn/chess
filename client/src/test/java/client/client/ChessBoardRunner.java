package client;


public class ChessBoardRunner {

    static void main() {
        chess.ChessBoard board = new chess.ChessBoard();

        ui.ChessBoard boardDrawer = new ui.ChessBoard();

        boardDrawer.drawGame(board, true);
    }
}
