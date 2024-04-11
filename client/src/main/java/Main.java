import ServerFacade.ServerFacade;
import chess.*;
import ui.EscapeSequences;

import java.util.Scanner;

import static chess.ChessGame.TeamColor.WHITE;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("♕ 240 Chess Client. Type Help to get started. ♕ \n" );
        String currentState = "[LOGGED OUT] >>> ";
        String authToken = "";
        Scanner scanner = new Scanner(System.in);
        boolean quitProgram = false;
        while (!quitProgram) {
            System.out.print(currentState);
            String command = scanner.nextLine();
            command = command.toLowerCase();
            if (currentState.equals("[LOGGED OUT] >>> ")) {
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
                        String username = scanner.nextLine();
                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                        authToken = ServerFacade.login(username, password);
                        if (authToken != null) {
                            currentState = "[LOGGED IN] >>> ";
                        }
                        break;
                    case "register":
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        System.out.print("Username: ");
                        String username_register = scanner.nextLine();
                        System.out.print("Password: ");
                        String password_register = scanner.nextLine();
                        System.out.print("Email: ");
                        String email_register = scanner.nextLine();
                        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                        authToken = ServerFacade.register(username_register, password_register, email_register);
                        if (authToken != null) {
                            currentState = "[LOGGED IN] >>> ";
                        }
                }
            }
            else if (currentState.equals("[LOGGED IN] >>> ")) {
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
                        ServerFacade.logout(authToken);
                        currentState = "[LOGGED OUT] >>> ";
                        break;
                    case "create game":
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        System.out.print("Name for Game: ");
                        String gameName = scanner.nextLine();
                        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                        ServerFacade.createGame(gameName, authToken);
                        break;
                    case "list games":
                        ServerFacade.listGames(authToken);
                        break;
                    case "join game":
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        System.out.print("Game ID: ");
                        int gameID = Integer.parseInt(scanner.nextLine());
                        System.out.print("Team Color: ");
                        String teamColorString = scanner.nextLine().toLowerCase();
                        ChessGame.TeamColor teamColor = switch (teamColorString) {
                            case "black" -> ChessGame.TeamColor.BLACK;
                            default -> WHITE;
                        };
                        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                        ServerFacade.joinGame(gameID, teamColor, authToken);
                        currentState = "[GAME MODE] >>> ";
                        break;
                    case "join observer":
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        System.out.print("Game ID: ");
                        int gameID2 = Integer.parseInt(scanner.nextLine());
                        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                        ServerFacade.joinGame(gameID2, null, authToken);
                        currentState = "[GAME MODE] >>> ";
                        break;
                }
            }
            else {
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
                        quitProgram = true;
                        System.out.print("  Thank you for playing!");
                        break;
                    case "leave":
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        System.out.print("Username: ");
                        String username = scanner.nextLine();
                        System.out.print("Password: ");
                        String password = scanner.nextLine();
                        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                        authToken = ServerFacade.login(username, password);
                        if (authToken != null) {
                            currentState = "[LOGGED IN] >>> ";
                        }
                        break;
                    case "make move":
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        System.out.print("From row (1-8): ");
                        int current_row = Integer.parseInt(scanner.nextLine());
                        System.out.print("From col (a-h): ");
                        int current_col = (int) scanner.nextLine().charAt(0) - 96;
                        System.out.print("To row (1-8): ");
                        int to_row = Integer.parseInt(scanner.nextLine());
                        System.out.print("To col (a-h): ");
                        int to_col = (int) scanner.nextLine().charAt(0) - 96;
                        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                        ChessPosition fromPos = new ChessPosition(current_row, current_col);
                        ChessPosition toPos = new ChessPosition(to_row, to_col);
                        ChessMove move = new ChessMove(fromPos, toPos, null);
                        break;
                    case "resign":
                        break;
                    case "highlight":
                        break;
                }
            }
        }
    }





}