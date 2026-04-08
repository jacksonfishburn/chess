package ui;

import chess.ChessGame;

public class GameplayClient {

    private final ServerFacade server;
    private final ChessGame game;
    private final boolean isWhite;

    public GameplayClient(ServerFacade server, ChessGame game, boolean isWhite) {
        this.server = server;
        this.game = game;
        this.isWhite = isWhite;
    }

    public void run() {
        drawBoard(isWhite);
        label:
        while (true) {
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
                    System.out.println("Bye!\n");
                    break label;
                case "4":
                    System.out.println("Make Move");
                    break;
                case "5":
                    System.out.println("Resign");
                    break;
                case "6":
                    System.out.println("Highlight");
                    break;
                default:
                    System.out.println("\nInvalid Input. Enter a number 1-6.");
                    break;
            }
        }
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

    private void printMenu() {
        System.out.println("\n1. Help");
        System.out.println("2. Redraw Chess Board");
        System.out.println("3. Leave");
        System.out.println("4. Make Move");
        System.out.println("5. Resign");
        System.out.println("6. Highlight Legal Moves\n");
    }

    private void leave() {

    }

    public void drawBoard(boolean isWhite) {
        chess.ChessBoard board = game.getBoard();
        ui.ChessBoard boardDrawer = new ChessBoard();
        System.out.print("\n");
        boardDrawer.drawGame(board, isWhite);
    }
}
