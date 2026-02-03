package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator extends MoveCalculator {

    public KingMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves() {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        for (int i = -1; i < 2; i++){
            for (int k = -1; k < 2; k++){
                makeMove(possibleMoves, i, k);
            }
        }

        return possibleMoves;
    }

    private void makeMove(Collection<ChessMove> possibleMoves, int x, int y) {
        if (x == 0 && y == 0) {
            return;
        }
        ChessPosition pos = new ChessPosition(position.getRow() + x, position.getColumn() + y);
        addMove(possibleMoves, pos);
    }
}
