package chess;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;

public class MoveCalculator {
    protected ChessBoard board;
    protected ChessPosition position;
    protected ChessPiece piece;

    public MoveCalculator(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
        piece = board.getPiece(position);
    }

    protected boolean isOpponentPiece(ChessPosition pos) {
        return board.getPiece(pos).getTeamColor() != piece.getTeamColor();
    }

    protected boolean canMove(ChessPosition pos) {

        if (pos.getColumn() > 8 || pos.getRow() > 8) {
            return false;
        }
        if (pos.getColumn() < 0 || pos.getRow() < 0) {
            return false;
        }

        if (board.getPiece(pos) == null) {
            return true;
        }
        return isOpponentPiece(pos);
    }

    protected Collection<ChessMove> verticalMoves() {
        Collection<ChessMove> possibleMoves = List.of();
        for (int i = position.getRow(); i < 9; i++) {
            ChessPosition checkUp = new ChessPosition(i, position.getColumn());
            if (addMove(possibleMoves, checkUp)) {
                break;
            }
        }
        for (int i = position.getRow(); i > 0; i--) {
            ChessPosition checkDown = new ChessPosition(i, position.getColumn());
            if (addMove(possibleMoves, checkDown)) {
                break;
            }
        }
        return possibleMoves;
    }

    protected Collection<ChessMove> horizontalMoves() {
        Collection<ChessMove> possibleMoves = List.of();
        for (int i = position.getColumn(); i < 9; i++) {
            ChessPosition checkRight = new ChessPosition(position.getRow(), i);
            if (addMove(possibleMoves, checkRight)) {
                break;
            }
        }
        for (int i = position.getColumn(); i > 0; i--) {
            ChessPosition checkLeft = new ChessPosition(position.getRow(), i);
            if (addMove(possibleMoves, checkLeft)) {
                break;
            }
        }
        return possibleMoves;
    }


    private boolean addMove(Collection<ChessMove> possibleMoves, ChessPosition checkPos) {
        if (canMove(checkPos)) {
            ChessMove move = new ChessMove(position, checkPos, piece.getPieceType());
            possibleMoves.add(move);
        } else {
            return true;
        }
        return isOpponentPiece(position);
    }
}
