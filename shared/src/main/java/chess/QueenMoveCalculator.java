package chess;

import javax.swing.*;
import java.util.Collection;

public class QueenMoveCalculator extends MoveCalculator{

    public QueenMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }
     public Collection<ChessMove> moves() {

        Collection<ChessMove> possibleMoves = new BishopMoveCalculator(board, position).moves();
        possibleMoves.addAll(new RookMoveCalculator(board, position).moves());

        return possibleMoves;
     }
}
