package chess;

import java.util.Collection;
import java.util.Objects;

public class CastleLogic {
    CastleRights castleRights;
    private final ChessGame game;

    public CastleLogic(ChessGame game) {
        this.game = game;
        castleRights = new CastleRights();
    }

    public boolean kingSideOpen(ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? 1 : 8;
        ChessPosition startPos = new ChessPosition(i, 6);
        ChessPiece spot1  = game.getBoard().getPiece(startPos);
        ChessPiece spot2 = game.getBoard().getPiece(new ChessPosition(i, 7));

        if (spot1 == null && spot2 == null
                && castleRights.hasKingSideRight(team)) {
                return wontSeeCheck(i, 1, team);
        } return false;
    }

    public boolean queenSideOpen(ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? 1 : 8;
        ChessPosition startPos = new ChessPosition(i, 4);
        ChessPiece spot1 = game.getBoard().getPiece(startPos);
        ChessPiece spot2 = game.getBoard().getPiece(new ChessPosition(i, 3));
        ChessPiece spot3 = game.getBoard().getPiece(new ChessPosition(i, 2));

        if (spot1 == null && spot2 == null && spot3 == null
                && castleRights.hasQueenSideRight(team)) {
            return wontSeeCheck(i, -1, team);
        } return false;
    }

    private boolean wontSeeCheck(int row, int dir, ChessGame.TeamColor team) {
        ChessBoard boardBackup = game.getBoard().clone();
        ChessPiece king = game.getBoard().getPiece(new ChessPosition(row, 5));
        boolean wontSeeCheck = true;

        game.getBoard().addPiece(new ChessPosition(row, 5 + dir), king);
        game.getBoard().addPiece(new ChessPosition(row, 5 + dir*2), king);

        if (game.isInCheck(team)) {
            wontSeeCheck = false;
        }

        game.setBoard(boardBackup);
        return wontSeeCheck;
    }

    public void addKingSideMove(Collection<ChessMove> moves, ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? 1 : 8;
        ChessPosition kingStartPos = new ChessPosition(i, 5);
        ChessPosition kingEndPos = new ChessPosition(i, 7);

        ChessMove kingMove = new ChessMove(kingStartPos, kingEndPos, null);

        moves.add(kingMove);
    }

    public void addQueenSideMove(Collection<ChessMove> moves, ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? 1 : 8;
        ChessPosition kingStartPos = new ChessPosition(i, 5);
        ChessPosition kingEndPos = new ChessPosition(i, 3);

        ChessMove kingMove = new ChessMove(kingStartPos, kingEndPos, null);

        moves.add(kingMove);
    }

    public boolean isCastleMove(ChessMove move) {
        ChessPiece piece = game.getBoard().getPiece(move.startPosition());
        if (piece == null || !(piece.getPieceType() == ChessPiece.PieceType.KING)){
            return false;
        }
        int spacesMoved = Math.abs(move.startPosition().getColumn() - move.endPosition().getColumn());
        System.out.printf("returning is castle move: %b", spacesMoved == 2);
        return spacesMoved == 2;
    }

    public void moveRook(ChessMove kingMove, ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? 1 : 8;
        int moveDir = kingMove.startPosition().getColumn() - kingMove.endPosition().getColumn();
        ChessPosition startPos;
        ChessPosition endPos;
        if (moveDir < 0) {
            startPos = new ChessPosition(i, 8);
            endPos = new ChessPosition(i, 6);
        } else {
            startPos = new ChessPosition(i, 1);
            endPos = new ChessPosition(i, 4);
        }
        ChessPiece rook = game.getBoard().getPiece(startPos);

        game.getBoard().addPiece(startPos, null);
        game.getBoard().addPiece(endPos, rook);
    }

    public void checkCastling(ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? 1 : 8;
        ChessPosition king = new ChessPosition(i, 5);
        ChessPosition kingSideRook = new ChessPosition(i, 8);
        ChessPosition queenSideRook = new ChessPosition(i, 1);

        if (!Objects.equals(game.getBoard().getPiece(king), new ChessPiece(team, ChessPiece.PieceType.KING))) {
            castleRights.lostKingSideRight(team);
            castleRights.lostQueenSideRight(team);
        }
        if (!Objects.equals(game.getBoard().getPiece(kingSideRook), new ChessPiece(team, ChessPiece.PieceType.ROOK))) {
            castleRights.lostKingSideRight(team);
        }
        if (!Objects.equals(game.getBoard().getPiece(queenSideRook), new ChessPiece(team, ChessPiece.PieceType.ROOK))) {
            castleRights.lostQueenSideRight(team);
        }
    }
}
