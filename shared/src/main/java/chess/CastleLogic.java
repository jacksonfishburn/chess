package chess;

import java.util.Collection;
import java.util.Objects;

public class CastleLogic {
    CastleRights castleRights;
    private ChessBoard board;

    public CastleLogic(ChessBoard board) {
        this.board = board;
        castleRights = new CastleRights();
    }

    public boolean kingSideOpen(ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? 8 : 1;
        ChessPiece spot1 = board.getPiece(new ChessPosition(i, 6));
        ChessPiece spot2 = board.getPiece(new ChessPosition(i, 7));

        return spot1 == null && spot2 == null && castleRights.hasKingSideRight(team);
    }

    public boolean queenSideOpen(ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? 8 : 1;
        ChessPiece spot1 = board.getPiece(new ChessPosition(i, 2));
        ChessPiece spot2 = board.getPiece(new ChessPosition(i, 3));
        ChessPiece spot3 = board.getPiece(new ChessPosition(i, 4));

        return spot1 == null && spot2 == null && spot3 == null && castleRights.hasQueenSideRight(team);
    }

    public void checkCastling(ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? 8 : 1;
        ChessPosition king = new ChessPosition(i, 5);
        ChessPosition kingSideRook = new ChessPosition(i, 8);
        ChessPosition queenSideRook = new ChessPosition(i, 1);

        if (!Objects.equals(board.getPiece(king), new ChessPiece(team, ChessPiece.PieceType.KING))) {
            castleRights.lostKingSideRight(team);
            castleRights.lostQueenSideRight(team);
        }
        if (!Objects.equals(board.getPiece(kingSideRook), new ChessPiece(team, ChessPiece.PieceType.ROOK))) {
            castleRights.lostKingSideRight(team);
        }
        if (!Objects.equals(board.getPiece(queenSideRook), new ChessPiece(team, ChessPiece.PieceType.ROOK))) {
            castleRights.lostQueenSideRight(team);
        }
    }
}
