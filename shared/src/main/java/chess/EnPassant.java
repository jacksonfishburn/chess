package chess;

public class EnPassant {
    private final ChessGame game;
    private boolean canPassant;
    private ChessMove move;

    public EnPassant(ChessGame game) {
        this.game = game;
        canPassant = true;
    }

    public boolean opensPassant(ChessMove move) {
        ChessPosition endPos = move.getEndPosition();
        ChessPosition leftSpot = new ChessPosition(endPos.getRow(), endPos.getColumn() - 1);
        ChessPosition rightSpot = new ChessPosition(endPos.getRow(), endPos.getColumn() + 1);
        ChessPiece leftPiece = game.getBoard().getPiece(leftSpot);
        ChessPiece rightPiece = game.getBoard().getPiece(rightSpot);

        return (leftPiece.getPieceType() == ChessPiece.PieceType.PAWN
                || rightPiece.getPieceType() == ChessPiece.PieceType.PAWN)
                && pawnDoubleMove(move);
    }

    private boolean pawnDoubleMove(ChessMove move) {
        ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
        if (piece == null || !(piece.getPieceType() == ChessPiece.PieceType.PAWN)){
            return false;
        }
        int spacesMoved = Math.abs(move.getStartPosition().getRow() - move.getEndPosition().getRow());
        return spacesMoved == 2;
    }

    public void lostChance() {
        canPassant = false;
    }


}
