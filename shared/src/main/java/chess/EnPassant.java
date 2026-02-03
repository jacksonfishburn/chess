package chess;

public class EnPassant {
    private final ChessGame game;
    private ChessMove move;

    public EnPassant(ChessGame game) {
        this.game = game;
    }

    public void check(ChessMove move) {
        if (!pawnDoubleMove(move)){
            return;
        }
        createMove(move, 1);
        createMove(move, -1);
    }

    public void makePassantMove(ChessGame.TeamColor team) {
        int i = (team == ChessGame.TeamColor.WHITE) ? -1 : 1;
        ChessPosition endPos = move.getEndPosition();
        ChessPosition deadPos = new ChessPosition(endPos.getRow() + i, endPos.getColumn());
        game.getBoard().addPiece(deadPos, null);
    }

    private void createMove(ChessMove move, int side) {
        ChessPosition endPos = move.getEndPosition();
        ChessPosition spot = new ChessPosition(endPos.getRow(), endPos.getColumn() + side);
        ChessPiece piece = game.getBoard().getPiece(spot);

        if (piece == null) {
            return;
        }
        if (piece.getPieceType() != ChessPiece.PieceType.PAWN) {
            return;
        }
        ChessPosition startPos = move.getStartPosition();
        ChessGame.TeamColor team = game.getBoard().getPiece(startPos).getTeamColor();
        int i = (team == ChessGame.TeamColor.WHITE) ? 1 : -1;

        ChessPosition pass = new ChessPosition(startPos.getRow() + i, startPos.getColumn());
        this.move = new ChessMove(spot, pass, null);
    }

    private boolean pawnDoubleMove(ChessMove move) {
        ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
        if (piece.getPieceType() != ChessPiece.PieceType.PAWN){
            return false;
        }
        int spacesMoved = Math.abs(move.getStartPosition().getRow() - move.getEndPosition().getRow());
        return spacesMoved == 2;
    }

    public boolean isPassant(ChessMove move) {
        return this.move != null && this.move.equals(move);
    }

    public ChessMove getMove() {
        return move;
    }

    public void lostChance() {
        move = null;
    }
}
