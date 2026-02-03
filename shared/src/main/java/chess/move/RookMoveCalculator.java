package chess.move;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator extends MoveCalculator {

    public RookMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves() {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        addMovesInDirection(possibleMoves, 1, 0);
        addMovesInDirection(possibleMoves, -1, 0);
        addMovesInDirection(possibleMoves, 0, 1);
        addMovesInDirection(possibleMoves, 0, -1);

        return possibleMoves;
    }
}
