package ui;

import exceptions.OutOfRangeException;
import models.GameInfo;
import models.ListGameResult;
import models.SessionStartResult;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Client {

    private final ServerFacade server;
    private final Scanner scanner;

    public Client() {
        scanner = new Scanner(System.in);
        String url = "http://localhost:8080";
        server = new ServerFacade(url);
    }

    public void run() {
        System.out.println("___Welcome to Chess!___");
        label:
        while (true) {
            printStartMenu();

            String choice = getInput("-> ");

            switch (choice) {
                case "1":
                    printStartMenuHelp();
                    break;
                case "2":
                    System.out.println("Bye!\n");
                    break label;
                case "3":
                    login();
                    break;
                case "4":
                    register();
                    break;
                default:
                    System.out.println("\nInvalid Input. Enter a number 1-4.");
                    break;
            }
        }
    }

    private void printStartMenuHelp() {
        System.out.println("\nHelp: Menu option details");
        System.out.println("Quit: Exit the program");
        System.out.println("Login: Sign into chess account");
        System.out.println("Register: Create chess account");
    }

    private void printStartMenu() {
        System.out.println("\n1. Help");
        System.out.println("2. Quit");
        System.out.println("3. Login");
        System.out.println("4. Register\n");
    }

    private void login() {
        System.out.println("\n___Login___\n");
        String username = getInput("Username: ");
        String password = getInput("Password: ");

        try {
            SessionStartResult result = server.login(username, password);
            System.out.printf("\nWelcome back %s\n", result.username());
            mainMenu();
        } catch (Exception e) {
            System.out.printf("\n%s\n", e.getMessage());
        }
    }

    private void register() {
        System.out.println("\n___Register___\n");
        String username = getInput("Username: ");
        String password = getInput("Password: ");
        String email = getInput("Email: ");

        try {
            SessionStartResult result = server.register(username, password, email);
            System.out.printf("\nRegistered as %s\n", result.username());
            mainMenu();
        } catch (Exception e) {
            System.out.printf("\n%s\n", e.getMessage());
        }
    }

    private void mainMenu() {
        System.out.println("\n___Start Playing!___");
        label:
        while (true) {
            printMainMenu();
            String choice = getInput("-> ");

            switch (choice) {
                case "1":
                    printMainMenuHelp();
                    break;
                case "2":
                    logout();
                    break label;
                case "3":
                    createGame();
                    break;
                case "4":
                    listGames();
                    break;
                case "5":
                    playGame();
                    break;
                case "6":
                    observeGame();
                    break;
                default:
                    System.out.println("\nInvalid Input. Enter a number 1-6.");
                    break;
            }
        }
    }

    private void printMainMenuHelp() {
        System.out.println("\nHelp: Menu Option Details");
        System.out.println("Logout: Sign out of current account and return to start menu");
        System.out.println("Create Game: Create a chess game (doesn't join game)");
        System.out.println("List Games: See a list of all current games");
        System.out.println("Play Game: Join and play a chess game");
        System.out.println("Observe Game: View the state of a game");
    }

    private void printMainMenu() {
        System.out.println("\n1. Help");
        System.out.println("2. Logout");
        System.out.println("3. Create Game");
        System.out.println("4. List Games");
        System.out.println("5. Play Game");
        System.out.println("6. Observe Game\n");
    }

    private void logout() {
        try {
            server.logout();
            System.out.println("Bye!");
        } catch (Exception e) {
            System.out.printf("\n%s\n", e.getMessage());
        }
    }

    private void createGame() {
        String gameName = getInput("\nGame Name: ");
        try {
            server.createGame(gameName);
            System.out.printf("\n%s created\n", gameName);
        } catch (Exception e) {
            System.out.printf("\n%s\n", e.getMessage());
        }
    }

    private void listGames() {
        int i = 0;
        try {
            Collection<GameInfo> games = getGameList();
            System.out.println("\n___Games___");
            for (GameInfo game : games) {
                i++;
                String white = (game.whiteUsername() == null) ? "Open" : game.whiteUsername();
                String black = (game.blackUsername() == null) ? "Open" : game.blackUsername();
                System.out.printf("%d. %s | white: %s - black: %s%n",
                        i, game.gameName(), white, black);
            }
        } catch (Exception e) {
            System.out.printf("\n%s\n", e.getMessage());
        }
    }

    private List<GameInfo> getGameList() throws Exception {
        ListGameResult result = server.listGames();
        return (List<GameInfo>) result.games();
    }

    private void playGame() {
        String gameNum = getInput("\nGame Number: ");
        String inputColor = getInput("Team (white/1, black/2): ");

        try {
            List<GameInfo> games = getGameList();
            int gameIndex = Integer.parseInt(gameNum) - 1;
            ensureNumInRange(gameIndex, games.size() - 1);
            String color = getTeamColor(inputColor);
            boolean isWhite = Objects.equals(color, "WHITE");

            GameInfo game = games.get(gameIndex);

            server.joinGame(color, game.gameID());
            server.connectWS(game.gameID(), isWhite);

        } catch (NumberFormatException e) {
            System.out.println("\nYou must input a number");
        } catch (OutOfRangeException e) {
            System.out.println("\nA number you gave was invalid");
        } catch (Exception e) {
            System.out.printf("\nError: %s\n", e.getMessage());
        }
    }

    private void ensureNumInRange(int num, int range) {
        if (num < 0 || num > range) {
            throw new OutOfRangeException("");
        }
    }

    private String getTeamColor(String input) {
        if (Objects.equals(input, "white")) {
            return "WHITE";
        }
        if (Objects.equals(input, "black")) {
            return "BLACK";
        }
        int i = Integer.parseInt(input);
        if (i == 1) {
            return "WHITE";
        }
        if (i == 2) {
            return "BLACK";
        }
        else {
            throw new OutOfRangeException("");
        }
    }

    private String getInput(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }

    private void observeGame() {
        String gameNum = getInput("\nGame Number: ");

        try {
            List<GameInfo> games = getGameList();
            int gameIndex = Integer.parseInt(gameNum) - 1;
            ensureNumInRange(gameIndex, games.size() - 1);

            GameInfo game = games.get(gameIndex);

        } catch (NumberFormatException e) {
            System.out.println("\nYou must input a number");
        } catch (OutOfRangeException e) {
            System.out.println("\nA number you gave was invalid");
        } catch (Exception e) {
            System.out.printf("\n%s\n", e.getMessage());
        }
    }
}
