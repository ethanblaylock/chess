import chess.*;
import com.google.gson.Gson;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.RegisterResult;
import spark.utils.IOUtils;
import ui.EscapeSequences;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;
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
                        System.out.println("Go b");
                        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                        authToken = ServerFacade.register(username_register, password_register, email_register);
                        if (authToken != null) {
                            currentState = "[LOGGED IN] >>> ";
                        }
                }
            } else {
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
                        break;
                    case "join observer":
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        System.out.print("Game ID: ");
                        int gameID2 = Integer.parseInt(scanner.nextLine());
                        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                        ServerFacade.joinGame(gameID2, null, authToken);
                        break;
                }
            }
        }
    }



    static void makeChessBoard(ChessBoard board) {
        System.out.print(EscapeSequences.SET_TEXT_COLOR_DARK_GREY);
        int flipper = 0;
        for (int i = 0; i < 10; i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            if (i > 0 && i < 9) {
                char c = (char)(i+96);
                System.out.print(" " + c + " ");
            } else {
                System.out.print(EscapeSequences.EMPTY);
            }
        }
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        for (int i = 8; i >= 1; i--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            System.out.print(" " + i + " ");
            for (int j = 1; j <= 8; j++) {
                if (flipper == 1) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        System.out.print(board.getPiece(new ChessPosition(i, j)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                } else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        System.out.print(board.getPiece(new ChessPosition(i, j)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }
                }
                flipper = flipper ^ 1;
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            System.out.print(" " + i + " ");
            System.out.println(EscapeSequences.RESET_BG_COLOR);
            flipper = flipper ^ 1;
        }
        for (int i = 0; i < 10; i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            if (i > 0 && i < 9) {
                char c = (char)(i+96);
                System.out.print(" " + c + " ");
            } else {
                System.out.print(EscapeSequences.EMPTY);
            }
        }

        System.out.println(EscapeSequences.RESET_BG_COLOR);
        System.out.println(EscapeSequences.RESET_BG_COLOR);

        for (int i = 9; i >= 0; i--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            if (i > 0 && i < 9) {
                char c = (char)(i+96);
                System.out.print(" " + c + " ");
            } else {
                System.out.print(EscapeSequences.EMPTY);
            }
        }
        System.out.println(EscapeSequences.RESET_BG_COLOR);
        for (int i = 1; i <= 8; i++) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            System.out.print(" " + i + " ");
            for (int j = 8; j >= 1; j--) {
                if (flipper == 1) {
                    System.out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        System.out.print(board.getPiece(new ChessPosition(i, j)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }

                } else {
                    System.out.print(EscapeSequences.SET_BG_COLOR_LIGHT_GREY);
                    if (board.getPiece(new ChessPosition(i, j)) != null) {
                        System.out.print(board.getPiece(new ChessPosition(i, j)));
                    } else {
                        System.out.print(EscapeSequences.EMPTY);
                    }
                }
                flipper = flipper ^ 1;
            }
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            System.out.print(" " + i + " ");
            System.out.println(EscapeSequences.RESET_BG_COLOR);
            flipper = flipper ^ 1;
        }
        for (int i = 9; i >= 0; i--) {
            System.out.print(EscapeSequences.SET_BG_COLOR_GREY);
            if (i > 0 && i < 9) {
                char c = (char)(i+96);
                System.out.print(" " + c + " ");
            } else {
                System.out.print(EscapeSequences.EMPTY);
            }
        }

        System.out.println(EscapeSequences.RESET_BG_COLOR);
        System.out.println(EscapeSequences.RESET_TEXT_COLOR);
    }


}