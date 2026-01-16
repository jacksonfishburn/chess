package chess;

import java.util.Collection;
import java.util.List;

public class MoveCalculator {
    protected ChessBoard board;
    protected ChessPosition position;
    protected ChessPiece piece;

    public MoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        piece = board.getPiece(position);
    }

    protected boolean canMove(ChessPosition pos) {

        if (pos.getColumn() > 8 || pos.getRow() > 8) {
            return false;
        }
        if (pos.getColumn() < 0 || pos.getRow() < 0) {
            return false;
        }

        ChessPiece obstacle = board.getPiece(pos);
        if (obstacle == null) {
            return true;
        }
        return obstacle.getTeamColor() != piece.getTeamColor();
    }

    protected Collection<ChessMove> verticalMoves() {
        Collection<ChessMove> possibleMoves = List.of();
        for (int i = position.getRow(); i < 9; i++) {
            ChessPosition checkPos = new ChessPosition(i, position.getColumn());
            if (canMove(checkPos)) {
                ChessMove move = new ChessMove(position, checkPos, piece.getPieceType());
                possibleMoves.add(move);
            }
            else {
                break;
            }
        }
        for (int i = position.getRow(); i > 0; i--) {
            ChessPosition checkPos = new ChessPosition(i, position.getColumn());
            if (canMove(checkPos)) {
                ChessMove move = new ChessMove(position, checkPos, piece.getPieceType());
                possibleMoves.add(move);
            }
            else {
                break;
            }
        }
        return possibleMoves;
    }
}
