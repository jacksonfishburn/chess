package chess;

import java.util.Collection;

public class BishopMoveCalculator extends MoveCalculator{

    public BishopMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves() {
        return diagonalMoves();
    }
}
