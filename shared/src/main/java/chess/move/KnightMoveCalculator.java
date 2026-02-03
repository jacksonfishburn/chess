package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator extends MoveCalculator {

    public KnightMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves() {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        for (int i = 2; i >= -2; i--) {
            makeMoves(possibleMoves, i);
        }

        return possibleMoves;
    }

    private void makeMoves(Collection<ChessMove> possibleMoves, int i) {
        if (i == 0) {
            return;
        }
        ChessPosition pos1;
        ChessPosition pos2;
        if (Math.abs(i) == 2) {
            pos1 = new ChessPosition(position.getRow() + i, position.getColumn() + 1);
            pos2 = new ChessPosition(position.getRow() + i, position.getColumn() - 1);
        } else {
            pos1 = new ChessPosition(position.getRow() + i, position.getColumn() + 2);
            pos2 = new ChessPosition(position.getRow() + i, position.getColumn() - 2);
        }
        addMove(possibleMoves, pos1);
        addMove(possibleMoves, pos2);
    }
}
