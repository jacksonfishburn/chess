package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeoutException;

public class GameplayClient implements GameUpdateListener {

    private static final long GAME_LOAD_TIMEOUT_MS = 2000;

    private final ServerFacade server;
    private ChessGame game;
    private final int gameID;
    private final boolean isWhite;

    public GameplayClient(ServerFacade server, int gameID, boolean isWhite) {
        this.server = server;
        this.gameID = gameID;
        this.isWhite = isWhite;
    }

    public void run() {
        ServerMessageManager.addGameUpdateListener(this);
        try {
            label:
            while (true) {
                getGame();
                printMenu();
                String choice = Client.getInput("-> ");

                switch (choice) {
                    case "1":
                        printHelpMenu();
                        break;
                    case "2":
                        drawBoard(isWhite);
                        break;
                    case "3":
                        leave();
                        break label;
                    case "4":
                        makeMove();
                        break;
                    case "5":
                        if (resign()) {
                            break label;
                        }
                        break;
                    case "6":
                        highlightLegalMoves();
                        break;
                    default:
                        System.out.println("\nInvalid Input. Enter a number 1-6.");
                        break;
                }
            }
        } finally {
            ServerMessageManager.removeGameUpdateListener(this);
        }
    }

    public void observe() {
        ServerMessageManager.addGameUpdateListener(this);
        try {
            label:
            while (true) {
                getGame();
                printObserveMenu();
                String choice = Client.getInput("-> ");

                switch (choice) {
                    case "1":
                        printObserveHelpMenu();
                        break;
                    case "2":
                        drawBoard(isWhite);
                        break;
                    case "3":
                        leave();
                        break label;
                    case "4":
                        highlightLegalMoves();
                        break;
                    default:
                        System.out.println("\nInvalid Input. Enter a number 1-6.");
                        break;
                }
            }
        } finally {
            ServerMessageManager.removeGameUpdateListener(this);
        }
    }


    private void printMenu() {
        System.out.println("\n1. Help");
        System.out.println("2. Redraw Chess Board");
        System.out.println("3. Leave");
        System.out.println("4. Make Move");
        System.out.println("5. Resign");
        System.out.println("6. Highlight Legal Moves\n");
    }

    private void printObserveMenu() {
        System.out.println("\n1. Help");
        System.out.println("2. Redraw Chess Board");
        System.out.println("3. Leave");
        System.out.println("4. Highlight Legal Moves\n");
    }

    private void printHelpMenu() {
        System.out.println("\nHelp: Menu option details");
        System.out.println("Redraw Chess Board: Displays current game board");
        System.out.println("Leave: Exit game spot without ending game");
        System.out.println("Make Move: Enter move to make in game");
        System.out.println("Resign: Exit game and accept defeat");
        System.out.println("Highlight Legal Moves: " +
                "Displays chess board with a selected piece's legal moves highlighted");
    }

    private void printObserveHelpMenu() {
        System.out.println("\nHelp: Menu option details");
        System.out.println("Redraw Chess Board: Displays current game board");
        System.out.println("Leave: Exit game spot without ending game");
        System.out.println("Highlight Legal Moves: " +
                "Displays chess board with a selected piece's legal moves highlighted");
    }

    private void leave() {
        server.leaveGame(gameID);
        System.out.println("You left the game");
    }

    private void makeMove() {
        try {
            String startInput = Client.getInput("From: ");
            String endInput = Client.getInput("To: ");

            ChessMove move = makeMoveFromInput(startInput, endInput);
            server.makeMove(gameID, move);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid position. Use letter number format (a1, h8, d5).");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean resign() {
        String confirmation = Client.getInput("Are you sure you want to resign? (y/n): ");
        if (confirmation == null) {
            System.out.println("Resign canceled.");
            return false;
        }

        String value = confirmation.trim().toLowerCase();
        if (!value.equals("y")) {
            System.out.println("Resign canceled.");
            return false;
        }

        try {
            server.resign(gameID);
            System.out.println("You resigned");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private void highlightLegalMoves() {
        try {
            if (game == null) {
                getGame();
            }
            if (game == null) {
                System.out.println("Couldn't load game, try again with redraw.");
                return;
            }

            String pieceInput = Client.getInput("Piece Position: ");
            ChessPosition selectedPosition = parsePosition(pieceInput);
            Collection<ChessMove> legalMoves = game.validMoves(selectedPosition);
            if (legalMoves == null) {
                legalMoves = Collections.emptyList();
            }

            ChessBoard boardDrawer = new ChessBoard();
            System.out.print("\n");
            if (legalMoves.isEmpty()) {
                boardDrawer.highlightMoves(game.getBoard(), isWhite, selectedPosition, legalMoves);
            } else {
                boardDrawer.highlightMoves(game.getBoard(), isWhite, legalMoves);
            }
            System.out.print("\n-> ");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid position. Use letter number format (a1, h8, d5).");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private ChessMove makeMoveFromInput(String startInput, String endInput) {
        ChessPosition start = parsePosition(startInput);
        ChessPosition end = parsePosition(endInput);

        ChessPiece.PieceType promotionPiece = getPromotionPiece(start, end);

        return new ChessMove(start, end, promotionPiece);
    }

    private ChessPiece.PieceType getPromotionPiece(ChessPosition start, ChessPosition end) {
        int endRow = end.getRow();
        if (endRow != 1 && endRow != 8) {
            return null;
        }

        if (game == null) {
            getGame();
        }
        if (game == null || game.getBoard() == null) {
            return null;
        }

        ChessPiece movingPiece = game.getBoard().getPiece(start);
        if (movingPiece == null || movingPiece.getPieceType() != ChessPiece.PieceType.PAWN) {
            return null;
        }

        String promotionInput = Client.getInput("Promote to (R/B/N/Q): ");
        return inputToPromotionPiece(promotionInput);
    }

    private ChessPiece.PieceType inputToPromotionPiece(String input) {
        if (input == null) {
            System.out.println("Invalid promotion piece. Defaulting to Queen.");
            return ChessPiece.PieceType.QUEEN;
        }

        String value = input.trim().toLowerCase();
        return switch (value) {
            case "q", "queen" -> ChessPiece.PieceType.QUEEN;
            case "r", "rook" -> ChessPiece.PieceType.ROOK;
            case "b", "bishop" -> ChessPiece.PieceType.BISHOP;
            case "n", "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> {
                System.out.println("Invalid promotion piece. Defaulting to Queen.");
                yield ChessPiece.PieceType.QUEEN;
            }
        };
    }

    private ChessPosition parsePosition(String input) {
        if (input == null) {
            throw new IllegalArgumentException("Invalid input");
        }

        String value = input.trim().toLowerCase();
        if (value.length() != 2) {
            throw new IllegalArgumentException("Invalid input");
        }

        char file = value.charAt(0);
        char rank = value.charAt(1);
        if (file < 'a' || file > 'h' || rank < '1' || rank > '8') {
            throw new IllegalArgumentException("Invalid input");
        }

        int col = file - 'a' + 1;
        int row = rank - '0';
        return new ChessPosition(row, col);
    }

    public void drawBoard(boolean isWhite) {
        getGame();
        if (game == null) {
            System.out.println("Couldn't load game, try again with redraw.");
            return;
        }

        chess.ChessBoard board = game.getBoard();
        ui.ChessBoard boardDrawer = new ChessBoard();
        System.out.print("\n");
        boardDrawer.drawGame(board, isWhite);
        System.out.print("\n-> ");
    }

    public void getGame() {
        try {
            game = ServerMessageManager.getGame(GAME_LOAD_TIMEOUT_MS);
        } catch (TimeoutException e) {
            game = null;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            game = null;
        }
    }

    @Override
    public void onGameUpdated(ChessGame game) {
        this.game = game;
        drawBoard(isWhite);
    }
}