import ServerFacade.ServerFacade;
import chess.*;
import model.GameData;
import ui.EscapeSequences;
import ui.MakeBoard;

import java.util.Scanner;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static String currentState = "[LOGGED OUT] >>> ";
    static String authToken = "";
    static String username = "";
    static int gameID = 0;
    static ChessGame.TeamColor teamColor = null;
    static ServerFacade serverFacade = new ServerFacade();
    static boolean quitProgram = false;
    public static void main(String[] args) {
        System.out.println("♕ 240 Chess Client. Type Help to get started. ♕ \n" );
        while (!quitProgram) {
            System.out.print(currentState);
            String command = scanner.nextLine();
            command = command.toLowerCase();
            try {
                if (currentState.equals("[LOGGED OUT] >>> ")) {
                    loggedOut(command);
                } else if (currentState.equals("[LOGGED IN] >>> ")) {
                    loggedIn(command);
                } else {
                    gameMode(command);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void loggedOut(String command) throws Exception {
        switch (command) {
            case "help":
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  register ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- to create an account");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  login ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- to play chess");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  quit ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- playing chess");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  help ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- with possible commands");
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                break;
            case "quit":
                quitProgram = true;
                System.out.print("  Thank you for playing!");
                break;
            case "login":
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("Username: ");
                username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                authToken = serverFacade.login(username, password);
                if (authToken != null) {
                    currentState = "[LOGGED IN] >>> ";
                }
                break;
            case "register":
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("Username: ");
                String usernameRegister = scanner.nextLine();
                System.out.print("Password: ");
                String passwordRegister = scanner.nextLine();
                System.out.print("Email: ");
                String emailRegister = scanner.nextLine();
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                authToken = serverFacade.register(usernameRegister, passwordRegister, emailRegister);
                if (authToken != null) {
                    currentState = "[LOGGED IN] >>> ";
                }
        }
    }

    public static void loggedIn(String command) throws Exception {
        switch (command) {
            case "help":
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  logout ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- to logout");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  create game ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- to create a game");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  list games ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- to list the current games");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  join game ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- join a current game");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  join observer ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- join a current game as observer");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  help ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- with possible commands");
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                break;
            case "logout":
                serverFacade.logout(authToken);
                currentState = "[LOGGED OUT] >>> ";
                break;
            case "create game":
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("Name for Game: ");
                String gameName = scanner.nextLine();
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                serverFacade.createGame(gameName, authToken);
                break;
            case "list games":
                serverFacade.listGames(authToken);
                break;
            case "join game":
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("Game ID: ");
                gameID = Integer.parseInt(scanner.nextLine());
                System.out.print("Team Color: ");
                String teamColorString = scanner.nextLine().toLowerCase();
                teamColor = switch (teamColorString) {
                    case "black" -> BLACK;
                    case "white" -> WHITE;
                    default -> throw new IllegalStateException("Unexpected value: " + teamColorString);
                };
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                serverFacade.joinGame(gameID, teamColor, authToken, username);
                currentState = "[GAME MODE] >>> ";
                break;
            case "join observer":
                teamColor = null;
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("Game ID: ");
                gameID = Integer.parseInt(scanner.nextLine());
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                serverFacade.joinGame(gameID, null, authToken, username);
                currentState = "[GAME MODE] >>> ";
                break;
        }
    }

    public static void gameMode(String command) throws Exception {
        switch (command) {
            case "help":
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  redraw ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- to redraw the board");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  leave ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- to leave the game");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  make move ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- to make a move");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  resign ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- forfeit the game");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  highlight ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- highlights legal moves");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("  help ");
                System.out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                System.out.println("- with possible commands");
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                break;
            case "redraw":
                serverFacade.redrawBoard();
                break;
            case "leave":
                serverFacade.leave();
                currentState = "[LOGGED IN] >>> ";
                break;
            case "make move":
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                if (serverFacade.getGame(authToken, gameID).isGameOver()) {
                    System.out.println("Game is over, no more moves can be made");
                    break;
                }
                else if (serverFacade.getGame(authToken, gameID).getTeamTurn() != teamColor) {
                    System.out.println("Not your turn yet");
                    break;
                }
                System.out.print("From col (a-h): ");
                int currentCol = (int) scanner.nextLine().charAt(0) - 96;
                System.out.print("From row (1-8): ");
                int currentRow = Integer.parseInt(scanner.nextLine());
                System.out.print("To col (a-h): ");
                int toCol = (int) scanner.nextLine().charAt(0) - 96;
                System.out.print("To row (1-8): ");
                int toRow = Integer.parseInt(scanner.nextLine());
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                ChessPosition fromPos = new ChessPosition(currentRow, currentCol);
                ChessPosition toPos = new ChessPosition(toRow, toCol);
                ChessMove move = new ChessMove(fromPos, toPos, null);
                serverFacade.makeMove(move);
                break;
            case "resign":
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.print("Are you sure? (y/n) ");
                String input = scanner.nextLine().toLowerCase();
                if (input.equals("y")) {
                    serverFacade.resign();
                }
                break;
            case "highlight":
                System.out.println("Select which piece to see moves for");
                System.out.print("col (a-h): ");
                int hilCol = (int) scanner.nextLine().charAt(0) - 96;
                System.out.print("row (1-8): ");
                int hilRow = Integer.parseInt(scanner.nextLine());
                MakeBoard.highlightMove(serverFacade.getGame(authToken, gameID).getBoard(), teamColor, serverFacade.getGame(authToken, gameID).validMoves(new ChessPosition(hilRow, hilCol)));
                break;
        }
    }

}