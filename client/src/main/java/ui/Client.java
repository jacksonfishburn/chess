package ui;

import models.models.CreateGameRequest;

import java.util.Scanner;

public class Client {

    ServerFacade server;
    Scanner scanner;

    public Client() {
        scanner = new Scanner(System.in);
        server = new ServerFacade();
    }

    public void run() {
        System.out.println("___Welcome to Chess!___");
        label:
        while (true) {
            printStartMenu();

            String choice = getInput("-> ");

            switch (choice) {
                case "1":
                    System.out.println("\nHere's some help!");
                    break;
                case "2":
                    System.out.println("Bye!");
                    break label;
                case "3":
                    login();
                    break;
                case "4":
                    register();
                    break;
                default:
                    System.out.println("Invalid Input. Enter a number 1-4.");
                    break;
            }
        }
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

        if (server.login(username, password)) {
            mainMenu();
        }
        return;
    }

    private void register() {
        System.out.println("\n___Register___\n");
        String username = getInput("Username: ");
        String password = getInput("Password: ");

        server.register(username, password);
    }

    private void mainMenu() {
        System.out.println("\n___Start Playing!___");
        label:
        while (true) {
            printMainMenu();
            String choice = getInput("-> ");

            switch (choice) {
                case "1":
                    System.out.println("\nHere's some help!");
                    break;
                case "2":
                    System.out.println("\nBye!");
                    break label;
                case "3":
                    createGame();
                    break;
                case "4":
                    System.out.println("\nlist games");
                    break;
                case "5":
                    System.out.println("\nplay game");
                    break;
                case "6":
                    System.out.println("\nobserve game");
                    break;
                default:
                    System.out.println("Invalid Input. Enter a number 1-6.");
                    break;
            }
        }
    }

    private void createGame() {
        String gameName = getInput("\nGame Name: ");

        server.createGame(gameName);

        System.out.printf("\nGame Created: %s%n", gameName);
    }

    private void printMainMenu() {
        System.out.println("\n1. Help");
        System.out.println("2. Logout");
        System.out.println("3. Create Game");
        System.out.println("4. List Games");
        System.out.println("5. Play Game");
        System.out.println("6. Observe Game\n");
    }



    private String getInput(String label) {
        System.out.print(label);
        return scanner.nextLine().trim();
    }
}
