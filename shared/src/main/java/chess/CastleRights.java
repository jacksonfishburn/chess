package chess;

public class CastleRights {
    private boolean whiteKingSide;
    private boolean whiteQueenSide;
    private boolean blackKingSide;
    private boolean blackQueenSide;

    public CastleRights () {
        whiteQueenSide = true;
        whiteKingSide = true;
        blackQueenSide = true;
        blackKingSide = true;
    }

    public boolean hasQueenSideRight(ChessGame.TeamColor team) {
        if (team == ChessGame.TeamColor.WHITE){
            return whiteQueenSide;
        } else {
            return blackQueenSide;
        }
    }

    public boolean hasKingSideRight(ChessGame.TeamColor team) {
        if (team == ChessGame.TeamColor.WHITE){
            return whiteKingSide;
        } else {
            return blackKingSide;
        }
    }

    public void lostQueenSideRight(ChessGame.TeamColor team) {
        if (team == ChessGame.TeamColor.WHITE){
            whiteQueenSide = false;
        } else {
            blackQueenSide = false;
        }
    }

    public void lostKingSideRight(ChessGame.TeamColor team) {
        if (team == ChessGame.TeamColor.WHITE){
            whiteKingSide = false;
        } else {
            blackKingSide = false;
        }
    }
}
