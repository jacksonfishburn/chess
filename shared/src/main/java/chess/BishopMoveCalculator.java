package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator extends MoveCalculator{

    public BishopMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves() {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        addMovesInDirection(possibleMoves, 1, 1);
        addMovesInDirection(possibleMoves, -1, 1);
        addMovesInDirection(possibleMoves, 1, -1);
        addMovesInDirection(possibleMoves, -1, -1);

        return possibleMoves;
    }
}
