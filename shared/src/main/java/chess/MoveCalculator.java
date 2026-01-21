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

    protected boolean isOpponentPiece(ChessPosition pos) {
        return board.getPiece(pos).getTeamColor() != piece.getTeamColor();
    }

    protected boolean canMove(ChessPosition pos) {

        if (pos.getColumn() > 8 || pos.getRow() > 8) {
            return false;
        }
        if (pos.getColumn() < 1 || pos.getRow() < 1) {
            return false;
        }

        if (board.getPiece(pos) == null) {
            return true;
        }
        return isOpponentPiece(pos);
    }

    protected Collection<ChessMove> straitMoves() {
        Collection<ChessMove> possibleMoves = List.of();

        findMoves(possibleMoves, 1, 0);
        findMoves(possibleMoves, -1, 0);
        findMoves(possibleMoves, 0, 1);
        findMoves(possibleMoves, 0, -1);

        return possibleMoves;
    }

    protected Collection<ChessMove> diagonalMoves() {
        Collection<ChessMove> possibleMoves = List.of();

        findMoves(possibleMoves, 1, 1);
        findMoves(possibleMoves, -1, 1);
        findMoves(possibleMoves, 1, -1);
        findMoves(possibleMoves, -1, -1);

        return possibleMoves;
    }

    private void findMoves(Collection<ChessMove> possibleMoves, int x, int y) {
        int incrementX = x;
        int incrementY = y;
        for (int i = 0; i < 9; i++) {
            ChessPosition checkPos = new ChessPosition(position.getRow() + x, position.getColumn() + y);
            if (addMove(possibleMoves, checkPos)) {
                break;
            }
            x = x + incrementX;
            y = y + incrementY;
        }
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
