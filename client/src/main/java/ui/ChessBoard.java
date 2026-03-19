package ui;


import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;


public class ChessBoard {

    private final static List<String> files = new ArrayList<>(
            List.of(" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "));

    private int startRow;
    private int startCol;
    private int direction;
    private chess.ChessBoard board;

    public void drawGame(chess.ChessBoard board, boolean isWhitePlayer) {
        startRow = 1;
        startCol = 8;
        direction = 1;
        if (isWhitePlayer) {
            startRow = 8;
            startCol = 1;
            direction = -1;
        }

        this.board = board;

        drawFiles();
        drawBoard();
        drawFiles();
    }

    private void drawFiles() {
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_LIGHT_GREY);

        int i = startRow - 1;
        System.out.print("   ");
        for (int x = 0; x < 8; x++) {
            System.out.print(files.get(i));
            i += direction;
        }
        System.out.print("   ");
        System.out.print(RESET_BG_COLOR);
        System.out.print("\n");
    }

    private void drawBoard() {
        int rowTally = 0;
        for (int currentRow = startRow; rowTally < 8; currentRow += direction) {
            rowTally++;
            int colTally = 0;

            drawRank(currentRow);

            for (int currentCol = startCol; colTally < 8; currentCol -= direction) {
                colTally++;

                drawSquare(currentRow, currentCol);
                System.out.print(RESET_TEXT_BOLD_FAINT);
            }
            drawRank(currentRow);

            System.out.print(RESET_BG_COLOR);
            System.out.print("\n");
        }
    }

    private void drawRank(int i) {
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_LIGHT_GREY);

        System.out.printf(" %d ", i);
    }

    private void drawSquare(int row, int col) {
        String bgColor = determineBgColor(row, col);

        System.out.print(bgColor);
        drawPiece(row, col);
    }

    private String determineBgColor(int row, int col) {
        int total = row + col;
        if (total % 2 == 0) {
            return SET_BG_COLOR_DARK_GREEN;
        }
        return SET_BG_COLOR_BLUE;
    }

    private void drawPiece(int row, int col) {
        ChessPosition pos = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(pos);

        String pieceString = getPieceString(piece);
        System.out.print(SET_TEXT_BOLD);
        System.out.print(pieceString);
    }

    private String getPieceString(ChessPiece piece) {
        if (piece == null) {
            return "   ";
        }
        System.out.print(SET_TEXT_COLOR_BLACK);
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            System.out.print(SET_TEXT_COLOR_WHITE);
        }
        return findPiece(piece.getPieceType());
    }

    private String findPiece(ChessPiece.PieceType type) {
        switch (type) {
            case PAWN -> {
                return " p ";
            }
            case ROOK -> {
                return " R ";
            }
            case KNIGHT -> {
                return " N ";
            }
            case BISHOP -> {
                return " B ";
            }
            case QUEEN -> {
                return " Q ";
            }
            case KING -> {
                return " K ";
            }
            default -> {
                return "   ";
            }
        }
    }
}
