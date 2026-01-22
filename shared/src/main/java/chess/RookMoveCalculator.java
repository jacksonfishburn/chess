package chess;

import java.util.Collection;

public class RookMoveCalculator extends MoveCalculator {

    public RookMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves() {
        return straitMoves();
    }
}
