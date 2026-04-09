package ui;


import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ui.EscapeSequences.*;


public class ChessBoard {

    private final static List<String> FILES = new ArrayList<>(
            List.of(" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "));

    private int startRow;
    private int startCol;
    private int direction;
    private chess.ChessBoard board;

    public void drawGame(chess.ChessBoard board, boolean isWhite) {
        setStarts(isWhite);
        this.board = board;
        drawFiles();
        drawBoard(null, null);
        drawFiles();
    }

    public void highlightMoves(chess.ChessBoard board, boolean isWhite, Collection<ChessMove> moves) {
        ChessPosition selectedPosition = null;
        if (moves != null) {
            for (ChessMove move : moves) {
                if (move != null) {
                    selectedPosition = move.getStartPosition();
                    break;
                }
            }
        }
        highlightMoves(board, isWhite, selectedPosition, moves);
    }

    public void highlightMoves(chess.ChessBoard board, boolean isWhite,
                               ChessPosition selectedPosition, Collection<ChessMove> moves) {
        setStarts(isWhite);
        this.board = board;

        Set<ChessPosition> startSquares = new HashSet<>();
        if (selectedPosition != null) {
            startSquares.add(selectedPosition);
        }

        Set<ChessPosition> endSquares = new HashSet<>();
        if (moves != null) {
            for (ChessMove move : moves) {
                if (move == null) {
                    continue;
                }
                if (selectedPosition == null) {
                    startSquares.add(move.getStartPosition());
                }
                endSquares.add(move.getEndPosition());
            }
        }

        drawFiles();
        drawBoard(startSquares, endSquares);
        drawFiles();
    }

    private void setStarts(boolean isWhite) {
        startRow = 1;
        startCol = 8;
        direction = 1;
        if (isWhite) {
            startRow = 8;
            startCol = 1;
            direction = -1;
        }
    }

    private void drawFiles() {
        System.out.print(SET_BG_COLOR_DARK_GREY);
        System.out.print(SET_TEXT_COLOR_LIGHT_GREY);

        int i = startRow - 1;
        System.out.print("   ");
        for (int x = 0; x < 8; x++) {
            System.out.print(FILES.get(i));
            i += direction;
        }
        System.out.print("   ");
        System.out.print(RESET_BG_COLOR);
        System.out.print(RESET_TEXT_COLOR);
        System.out.print("\n");
    }

    private void drawBoard(Set<ChessPosition> startSquares, Set<ChessPosition> endSquares) {
        int rowTally = 0;
        for (int currentRow = startRow; rowTally < 8; currentRow += direction) {
            rowTally++;
            int colTally = 0;

            drawRank(currentRow);

            for (int currentCol = startCol; colTally < 8; currentCol -= direction) {
                colTally++;

                ChessPosition pos = new ChessPosition(currentRow, currentCol);
                boolean isStartSquare = startSquares != null && startSquares.contains(pos);
                boolean isEndSquare = endSquares != null && endSquares.contains(pos);
                drawSquare(currentRow, currentCol, isStartSquare, isEndSquare);
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

    private void drawSquare(int row, int col, boolean isStartSquare, boolean isEndSquare) {
        String bgColor = determineBgColor(row, col, isStartSquare, isEndSquare);

        System.out.print(bgColor);
        drawPiece(row, col, isStartSquare || isEndSquare);
    }

    private String determineBgColor(int row, int col, boolean isStartSquare, boolean isEndSquare) {
        if (isStartSquare) {
            return SET_BG_COLOR_YELLOW;
        }
        if (isEndSquare) {
            return isLightSquare(row, col) ? SET_BG_COLOR_WHITE : SET_BG_COLOR_GREEN;
        }
        return determineBgColor(row, col);
    }

    private String determineBgColor(int row, int col) {
        int total = row + col;
        if (total % 2 == 0) {
            return SET_BG_COLOR_DARK_GREEN;
        }
        return SET_BG_COLOR_BLUE;
    }

    private boolean isLightSquare(int row, int col) {
        return (row + col) % 2 != 0;
    }

    private void drawPiece(int row, int col, boolean highlightSquare) {
        ChessPosition pos = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(pos);

        String pieceString = getPieceString(piece, highlightSquare);
        System.out.print(SET_TEXT_BOLD);
        System.out.print(pieceString);
    }

    private String getPieceString(ChessPiece piece, boolean highlightSquare) {
        if (piece == null) {
            return "   ";
        }
        System.out.print(SET_TEXT_COLOR_BLACK);
        if (!highlightSquare && piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
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
