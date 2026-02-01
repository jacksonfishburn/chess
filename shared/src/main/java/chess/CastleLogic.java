package chess;

import java.util.Collection;
import java.util.Objects;

public class CastleLogic {
    private boolean whiteHasCastle;
    private boolean blackHasCastle;
    private ChessBoard board;

    private boolean castleOpen(ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? 8 : 1;
        ChessPiece spot1 = board.getPiece(new ChessPosition(i, 6));
        ChessPiece spot2 = board.getPiece(new ChessPosition(i, 7));

        return hasCastle(team) && spot1 == null && spot2 == null;
    }

    private boolean hasCastle(ChessGame.TeamColor team) {
        if (team == ChessGame.TeamColor.WHITE){
            return whiteHasCastle;
        } else {
            return blackHasCastle;
        }
    }

    private void cantCastle(ChessGame.TeamColor team) {
        if (team == ChessGame.TeamColor.WHITE){
            whiteHasCastle = false;
        } else {
            blackHasCastle = false;
        }
    }

    private void checkCastling(ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? 8 : 1;
        ChessPosition king = new ChessPosition(i, 5);
        ChessPosition rook = new ChessPosition(i, 8);

        if (!Objects.equals(board.getPiece(king), new ChessPiece(team, ChessPiece.PieceType.KING)) ||
                !Objects.equals(board.getPiece(rook), new ChessPiece(team, ChessPiece.PieceType.ROOK))) {
            cantCastle(team);
        }
    }

//    private boolean posInDanger(ChessPosition pos, ChessGame.TeamColor team) {
//        ChessGame.TeamColor oppColor = (team == ChessGame.TeamColor.WHITE) ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
//
//        Collection<ChessMove> oppMoves = allTeamMoves(oppColor);
//
//        for (ChessMove move : oppMoves) {
//            ChessPosition danger = move.endPosition();
//            if (danger == pos){
//                return true;
//            }
//        } return false;
//    }


}
