package ui;


import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.BLACK_KING;
import static ui.EscapeSequences.BLACK_ROOK;

public class ChessBoard {

    private final static List<String> files = new ArrayList<>(
            List.of(" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "));

    private int startIndex;
    private int direction;
    private chess.ChessBoard board;

    public void drawGame(chess.ChessBoard board, boolean isWhitePlayer) {
        startIndex = 1;
        direction = 1;
        if (isWhitePlayer) {
            startIndex = 8;
            direction = -1;
        }

        this.board = board;

        drawFiles();
        drawBoard();
        drawFiles();
    }

    private void drawFiles() {
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        System.out.print(SET_TEXT_COLOR_BLACK);

        int i = startIndex - 1;
        System.out.print("   ");
        for (int x = 0; x < 8; x++) {
            System.out.print(files.get(i));
            i += direction;
        }
        System.out.print("   ");
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print("\n");
    }

    private void drawBoard() {
        int rowTally = 0;
        for (int currentRow = startIndex; rowTally < 8; currentRow += direction) {
            rowTally++;
            int colTally = 0;

            drawRank(currentRow);

            for (int currentCol = startIndex; colTally < 8; currentCol += direction) {
                colTally++;

                drawSquare(currentRow, currentCol);
            }
            drawRank(currentRow);

            System.out.print(SET_BG_COLOR_DARK_GREY);
            System.out.print("\n");
        }
    }

    private void drawRank(int i) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY);
        System.out.print(SET_TEXT_COLOR_BLACK);

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

            return SET_BG_COLOR_BLUE;
        }
        return SET_BG_COLOR_MAGENTA;
    }

    private void drawPiece(int row, int col) {
        ChessPosition pos = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(pos);

        String pieceString = getPieceString(piece);
        System.out.print(pieceString);
    }

    private String getPieceString(ChessPiece piece) {
        if (piece == null) {
            return "   ";
        }
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            return findWhitePiece(piece.getPieceType());
        }
        return findBlackPiece(piece.getPieceType());
    }

    private String findWhitePiece(ChessPiece.PieceType type) {
        switch (type) {
            case PAWN -> {
                return WHITE_PAWN;
            }
            case ROOK -> {
                return WHITE_ROOK;
            }
            case KNIGHT -> {
                return WHITE_KNIGHT;
            }
            case BISHOP -> {
                return WHITE_BISHOP;
            }
            case QUEEN -> {
                return WHITE_QUEEN;
            }
            case KING -> {
                return WHITE_KING;
            }
            default -> {
                return "   ";
            }
        }
    }

    private String findBlackPiece(ChessPiece.PieceType type) {
        switch (type) {
            case PAWN -> {
                return BLACK_PAWN;
            }
            case ROOK -> {
                return BLACK_ROOK;
            }
            case KNIGHT -> {
                return BLACK_KNIGHT;
            }
            case BISHOP -> {
                return BLACK_BISHOP;
            }
            case QUEEN -> {
                return BLACK_QUEEN;
            }
            case KING -> {
                return BLACK_KING;
            }
            default -> {
                return "   ";
            }
        }
    }
}
