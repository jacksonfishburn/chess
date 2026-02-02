package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;


/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private final CastleLogic castleLogic;
    private ChessBoard board;
    private TeamColor playingTeam;

    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
        castleLogic = new CastleLogic(this);
    }

    public TeamColor getTeamTurn() {
        return playingTeam;
    }

    public void setTeamTurn(TeamColor team) {
        playingTeam = team;
    }

    public enum TeamColor {
        WHITE,
        BLACK
    }

    private boolean cantMove(ChessMove move, ChessPiece piece) {
        ChessBoard boardBackup = board.clone();

        board.addPiece(move.getStartPosition(), null);
        board.addPiece(move.getEndPosition(), piece);

        if (isInCheck(piece.getTeamColor())){
            board = boardBackup;
            return true;
        }

        setBoard(boardBackup);
        return false;
    }

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);

        moves.removeIf(move -> cantMove(move, piece));

        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (castleLogic.kingSideOpen(piece.getTeamColor())) {
                castleLogic.addKingSideMove(moves, piece.getTeamColor());
            }
            if (castleLogic.queenSideOpen(piece.getTeamColor())) {
                castleLogic.addQueenSideMove(moves, piece.getTeamColor());
            }
        }

        return moves;
    }


    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        ChessPiece piece = board.getPiece(move.getStartPosition());

        if (piece == null || !validMoves.contains(move) || piece.getTeamColor() != playingTeam){
            throw new InvalidMoveException("Invalid Move");
        }

        if (move.getPromotionPiece() != null) {
            piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }

        if (castleLogic.isCastleMove(move)) {
            castleLogic.moveRook(move, playingTeam);
        }

        board.addPiece(move.getStartPosition(), null);
        board.addPiece(move.getEndPosition(), piece);

        castleLogic.checkCastling(playingTeam);

        if (playingTeam == TeamColor.WHITE) {
            playingTeam = TeamColor.BLACK;
        } else {
            playingTeam = TeamColor.WHITE;
        }
    }

    private Collection<ChessMove> allTeamMoves(TeamColor team) {
        Collection<ChessMove> moves = new ArrayList<>();

        for (int x = 1; x < 9; x++){
            for (int y = 1; y < 9; y++) {
                ChessPosition pos = new ChessPosition(x, y);
                ChessPiece piece = board.getPiece(pos);

                if (piece != null && piece.getTeamColor() == team) {
                    moves.addAll(piece.pieceMoves(board, pos));
                }
            }
        } return moves;
    }

    public boolean isInCheck(TeamColor teamColor) {
        TeamColor oppColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;

        Collection<ChessMove> oppMoves = allTeamMoves(oppColor);

        for (ChessMove move : oppMoves) {
            ChessPosition pos = move.getEndPosition();
            if (Objects.equals(board.getPiece(pos), new ChessPiece(teamColor, ChessPiece.PieceType.KING))) {
                return true;
            }
        } return false;
    }

    private boolean hasNoLegalMove(TeamColor teamColor) {
        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {
                ChessPosition pos = new ChessPosition(x, y);
                ChessPiece piece = board.getPiece(pos);
                if (piece == null) {
                    continue;
                }
                if (piece.getTeamColor() != teamColor) {
                    continue;
                }
                if (!validMoves(pos).isEmpty()) {
                    return false;
                }
            }
        } return true;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }
        return hasNoLegalMove(teamColor);
    }


    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        return hasNoLegalMove(teamColor);
    }


    public void setBoard(ChessBoard board) {
        this.board = board;
    }


    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && playingTeam == chessGame.playingTeam;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, playingTeam);
    }
}
