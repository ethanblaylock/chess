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
                        URI uri = new URI("http://localhost:8080/session");
                        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
                        http.setReadTimeout(5000);
                        http.setRequestMethod("POST");
                        http.setDoOutput(true);
                        // Make the request
                        http.connect();
                        try (var outputStream = http.getOutputStream()) {
                            var jsonBody = new Gson().toJson(new LoginRequest(username, password));
                            outputStream.write(jsonBody.getBytes());
                        }
                        try {
                            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                // Get HTTP response headers, if necessary
                                // Map<String, List<String>> headers = connection.getHeaderFields();

                                // OR

                                //connection.getHeaderField("Content-Length");

                                InputStream responseBody = http.getInputStream();
                                String jsonText = IOUtils.toString(responseBody);
                                authToken = new Gson().fromJson(jsonText, RegisterResult.class).authToken();
                                currentState = "[LOGGED IN] >>> ";
                                // Read response body from InputStream ...
                            } else {
                                System.out.println("hehe error");
                            }
                        } catch (Exception e) {
                            System.out.println("haha caught");
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
                        URI uri2 = new URI("http://localhost:8080/user");
                        HttpURLConnection http2 = (HttpURLConnection) uri2.toURL().openConnection();
                        http2.setReadTimeout(5000);
                        http2.setRequestMethod("POST");
                        http2.setDoOutput(true);
                        // Make the request
                        http2.connect();
                        try (var outputStream = http2.getOutputStream()) {
                            var jsonBody = new Gson().toJson(new RegisterRequest(username_register, password_register, email_register));
                            outputStream.write(jsonBody.getBytes());
                        }
                        try {
                            if (http2.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                // Get HTTP response headers, if necessary
                                // Map<String, List<String>> headers = connection.getHeaderFields();

                                // OR

                                //connection.getHeaderField("Content-Length");

                                InputStream responseBody = http2.getInputStream();
                                String jsonText = IOUtils.toString(responseBody);
                                authToken = new Gson().fromJson(jsonText, RegisterResult.class).authToken();
                                currentState = "[LOGGED IN] >>> ";
                                // Read response body from InputStream ...
                            } else {
                                System.out.println("hehe error");
                            }
                        } catch (Exception e) {
                            System.out.println("haha caught");
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
                        currentState = "[LOGGED OUT] >>> ";
                        break;
                    case "create game":
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        System.out.print("Name for Game: ");
                        String gameName = scanner.nextLine();
                        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                        URI uri2 = new URI("http://localhost:8080/game");
                        HttpURLConnection http2 = (HttpURLConnection) uri2.toURL().openConnection();
                        http2.setReadTimeout(5000);
                        http2.setRequestMethod("POST");
                        http2.setDoOutput(true);
                        // Make the request
                        http2.addRequestProperty("Authorization", authToken);
                        http2.connect();

                        try (var outputStream = http2.getOutputStream()) {
                            var jsonBody = new Gson().toJson(new CreateGameRequest(gameName));
                            outputStream.write(jsonBody.getBytes());
                        }
                        try {
                            if (http2.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                // Get HTTP response headers, if necessary
                                // Map<String, List<String>> headers = connection.getHeaderFields();

                                // OR

                                //connection.getHeaderField("Content-Length");

                                InputStream responseBody = http2.getInputStream();
                                String jsonText = IOUtils.toString(responseBody);
                                int gameID = new Gson().fromJson(jsonText, CreateGameResult.class).gameID();
                                System.out.println("Success! Game ID is: " + gameID);
                            } else {
                                System.out.println(http2.getResponseCode());
                                System.out.println(http2.getResponseMessage());
                            }
                        } catch (Exception e) {
                            System.out.println("haha caught");
                        }
                        break;
                    case "list games":
                        URI uri = new URI("http://localhost:8080/game");
                        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
                        http.setReadTimeout(5000);
                        http.setRequestMethod("GET");
                        http.setDoOutput(true);
                        // Make the request
                        http.addRequestProperty("Authorization", authToken);
                        http.connect();

                        try {
                            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                // Get HTTP response headers, if necessary
                                // Map<String, List<String>> headers = connection.getHeaderFields();

                                // OR

                                //connection.getHeaderField("Content-Length");

                                InputStream responseBody = http.getInputStream();
                                String jsonText = IOUtils.toString(responseBody);
                                Collection<GameData> games = new Gson().fromJson(jsonText, ListGamesResult.class).games();
                                System.out.println(games);
                            } else {
                                System.out.println(http.getResponseCode());
                                System.out.println(http.getResponseMessage());
                            }
                        } catch (Exception e) {
                            System.out.println("haha caught");
                        }
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
                        URI uri3 = new URI("http://localhost:8080/game");
                        HttpURLConnection http3 = (HttpURLConnection) uri3.toURL().openConnection();
                        http3.setReadTimeout(5000);
                        http3.setRequestMethod("POST");
                        http3.setDoOutput(true);
                        // Make the request
                        http3.addRequestProperty("Authorization", authToken);
                        http3.connect();

                        try (var outputStream = http3.getOutputStream()) {
                            var jsonBody = new Gson().toJson(new JoinGameRequest(teamColor, gameID));
                            outputStream.write(jsonBody.getBytes());
                        }
                        try {
                            if (http3.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                // Get HTTP response headers, if necessary
                                // Map<String, List<String>> headers = connection.getHeaderFields();

                                // OR

                                //connection.getHeaderField("Content-Length");
                                System.out.println("Success!");
                                ChessBoard board = new ChessBoard();
                                board.resetBoard();
                                makeChessBoard(board);
                            } else {
                                System.out.println(http3.getResponseCode());
                                System.out.println(http3.getResponseMessage());
                            }
                        } catch (Exception e) {
                            System.out.println("haha caught");
                        }
                        break;
                    case "join observer":
                        System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                        System.out.print("Game ID: ");
                        int gameID2 = Integer.parseInt(scanner.nextLine());
                        System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                        URI uri4 = new URI("http://localhost:8080/game");
                        HttpURLConnection http4 = (HttpURLConnection) uri4.toURL().openConnection();
                        http4.setReadTimeout(5000);
                        http4.setRequestMethod("POST");
                        http4.setDoOutput(true);
                        // Make the request
                        http4.addRequestProperty("Authorization", authToken);
                        http4.connect();

                        try (var outputStream = http4.getOutputStream()) {
                            var jsonBody = new Gson().toJson(new JoinGameRequest(null, gameID2));
                            outputStream.write(jsonBody.getBytes());
                        }
                        try {
                            if (http4.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                // Get HTTP response headers, if necessary
                                // Map<String, List<String>> headers = connection.getHeaderFields();

                                // OR

                                //connection.getHeaderField("Content-Length");
                                System.out.println("Success!");
                                ChessBoard board = new ChessBoard();
                                board.resetBoard();
                                makeChessBoard(board);
                            } else {
                                System.out.println(http4.getResponseCode());
                                System.out.println(http4.getResponseMessage());
                            }
                        } catch (Exception e) {
                            System.out.println("haha caught");
                        }
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