package ui;


import java.util.ArrayList;
import java.util.List;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private final static List<String> files = new ArrayList<>(
            List.of(" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "));

    private int startIndex;
    private int direction;

    public void drawGame(chess.ChessBoard chessBoard, boolean isWhitePlayer) {
        startIndex = 1;
        direction = 1;
        if (isWhitePlayer) {
            startIndex = 8;
            direction = -1;
        }

        drawFiles();
        drawBoard(chessBoard);
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

    private void drawBoard(chess.ChessBoard chessBoard) {
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
        System.out.print("   ");
    }

    private String determineBgColor(int row, int col) {
        int total = row + col;
        if (total % 2 == 0) {
            return SET_BG_COLOR_WHITE;
        }
        return SET_BG_COLOR_BLACK;
    }
}
