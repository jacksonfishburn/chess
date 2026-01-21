package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends MoveCalculator {

    public PawnMoveCalculator(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    public Collection<ChessMove> moves() {
        Collection<ChessMove> possibleMoves = new ArrayList<>();

        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            forward(possibleMoves, 1);
            diagonals(possibleMoves, 1);
        } else {
            forward(possibleMoves, -1);
            diagonals(possibleMoves, -1);
        }

        return possibleMoves;
    }

    private void forward(Collection<ChessMove> possibleMoves, int i) {
        ChessPosition checkPos = new ChessPosition(position.getRow() + i, position.getColumn());
        if (!isOpponentPiece(checkPos)) {
            addMove(possibleMoves, checkPos);
            if (inStartingPosition() && i < 2 && i > -2) {
                forward(possibleMoves, i*2);
            }
        }
    }

    private void diagonals(Collection<ChessMove> possibleMoves, int i) {
        ChessPosition checkLeft = new ChessPosition(position.getRow() + i, position.getColumn() - 1);
        ChessPosition checkRight = new ChessPosition(position.getRow() + i, position.getColumn() + 1);
        if (isOpponentPiece(checkLeft)) {
            addMove(possibleMoves, checkLeft);
        }
        if (isOpponentPiece(checkRight)) {
            addMove(possibleMoves, checkRight);
        }
    }

    @Override
    protected boolean addMove(Collection<ChessMove> possibleMoves, ChessPosition checkPos) {
        if (canMove(checkPos)) {
            possibleMoves.add(makeMove(checkPos));
        } else {
            return false;
        }
        return !isOpponentPiece(checkPos);
    }

    private ChessMove makeMove(ChessPosition pos) {
        if (willPromote()) {
            return new ChessMove(position, pos, ChessPiece.PieceType.QUEEN);
        }
        return new ChessMove(position, pos, null);
    }

    private boolean willPromote(){
        if (position.getRow() == 2 && piece.getTeamColor() == ChessGame.TeamColor.BLACK)
            return true;
        return (position.getRow() == 7 && piece.getTeamColor() == ChessGame.TeamColor.WHITE);
    }

    private boolean inStartingPosition() {
        if (position.getRow() == 2 && piece.getTeamColor() == ChessGame.TeamColor.WHITE)
            return true;
        return (position.getRow() == 7 && piece.getTeamColor() == ChessGame.TeamColor.BLACK);
    }
}
