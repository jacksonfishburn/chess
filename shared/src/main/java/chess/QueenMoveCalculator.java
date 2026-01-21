package chess;

import javax.swing.*;
import java.util.Collection;

public class QueenMoveCalculator extends MoveCalculator{

    public QueenMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }
     public Collection<ChessMove> moves() {

        Collection<ChessMove> possibleMoves = straitMoves();
        Collection<ChessMove> diagonal = diagonalMoves();

        possibleMoves.addAll(diagonal);

        return possibleMoves;
     }
}
