package chess;

import java.util.Collection;

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
        if (board.getPiece(pos) == null) {
            return false;
        }
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

    protected boolean addMove(Collection<ChessMove> possibleMoves, ChessPosition checkPos) {
        if (canMove(checkPos)) {
            ChessMove move = new ChessMove(position, checkPos, null);
            possibleMoves.add(move);
        } else {
            return false;
        }
        return !isOpponentPiece(checkPos);
    }

    protected void addMovesInDirection(Collection<ChessMove> possibleMoves, int dx, int dy) {
        int incrementX = dx;
        int incrementY = dy;
        ChessPosition checkPos = new ChessPosition(position.getRow() + dx, position.getColumn() + dy);
        while (addMove(possibleMoves, checkPos)) {
            dx = dx + incrementX;
            dy = dy + incrementY;
            checkPos = new ChessPosition(position.getRow() + dx, position.getColumn() + dy);
        }
    }
}
