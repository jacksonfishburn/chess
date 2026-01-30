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

    private ChessBoard board;
    private TeamColor playingTeam;


    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return playingTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        playingTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     * param StartPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
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
        }
        return moves;
    }

    private boolean cantMove(ChessMove move) {
        ChessBoard boardBackup = board.clone();

        ChessPiece piece = board.getPiece(move.getStartPosition());
        boolean cantMove = false;

        board.addPiece(move.getStartPosition(), null);
        board.addPiece(move.getEndPosition(), piece);

        if (isInCheck(piece.getTeamColor())){
            cantMove = true;
        }

        board = boardBackup;
        return cantMove;
    }


    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) return null;
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);

        if (piece.getTeamColor() != playingTeam) return new ArrayList<>();

        moves.removeIf(this::cantMove);

        return moves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */

    public void makeMove(ChessMove move) throws InvalidMoveException {
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());

        if (validMoves.contains(move)) {
            ChessPiece piece = board.getPiece(move.getStartPosition());

            if (move.getPromotionPiece() != null) {
                piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
            }

            board.addPiece(move.getStartPosition(), null);
            board.addPiece(move.getEndPosition(), piece);

            if (playingTeam == TeamColor.WHITE) {
                playingTeam = TeamColor.BLACK;
            } else {
                playingTeam = TeamColor.WHITE;
            }

        } else {
            throw new InvalidMoveException("Invalid Move"); }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor oppColor = TeamColor.WHITE;
        if (teamColor == TeamColor.WHITE) oppColor = TeamColor.BLACK;

        Collection<ChessMove> oppMoves = allTeamMoves(oppColor);

        for (ChessMove move : oppMoves) {
            ChessPosition pos = move.getEndPosition();
            if (Objects.equals(board.getPiece(pos), new ChessPiece(teamColor, ChessPiece.PieceType.KING))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
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
