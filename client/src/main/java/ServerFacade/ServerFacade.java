package ServerFacade;

import chess.ChessBoard;
import chess.ChessGame;
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
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import static ui.makeBoard.makeChessBoard;

public class ServerFacade {
    public static String login(String username, String password) throws Exception {
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
                return new Gson().fromJson(jsonText, RegisterResult.class).authToken();
                // Read response body from InputStream ...
            } else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.println(http.getResponseCode());
                System.out.println(http.getResponseMessage());
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            }
        } catch (Exception e) {
            System.out.println("haha caught");
        }
        return null;
    }

    public static String register(String username, String password, String email) throws Exception {
        URI uri2 = new URI("http://localhost:8080/user");
        HttpURLConnection http2 = (HttpURLConnection) uri2.toURL().openConnection();
        http2.setReadTimeout(5000);
        http2.setRequestMethod("POST");
        http2.setDoOutput(true);
        // Make the request
        http2.connect();
        try (var outputStream = http2.getOutputStream()) {
            var jsonBody = new Gson().toJson(new RegisterRequest(username, password, email));
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
                return new Gson().fromJson(jsonText, RegisterResult.class).authToken();
                // Read response body from InputStream ...
            } else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.println(http2.getResponseCode());
                System.out.println(http2.getResponseMessage());
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            }
        } catch (Exception e) {
            System.out.println("haha caught");
        }
        return null;
    }

    public static void createGame(String gameName, String authToken) throws Exception{
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
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.println(http2.getResponseCode());
                System.out.println(http2.getResponseMessage());
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            }
        } catch (Exception e) {
            System.out.println("haha caught");
        }
    }

    public static void listGames(String authToken) throws Exception{
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
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.println(games);
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            } else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.println(http.getResponseCode());
                System.out.println(http.getResponseMessage());
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            }
        } catch (Exception e) {
            System.out.println("haha caught");
        }
    }

    public static void joinGame(int gameID, ChessGame.TeamColor teamColor, String authToken) throws Exception {
        URI uri3 = new URI("http://localhost:8080/game");
        HttpURLConnection http3 = (HttpURLConnection) uri3.toURL().openConnection();
        http3.setReadTimeout(5000);
        http3.setRequestMethod("PUT");
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
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE);
                System.out.println("Success!");
                ChessBoard board = new ChessBoard();
                board.resetBoard();
                makeChessBoard(board);
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            } else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.println(http3.getResponseCode());
                System.out.println(http3.getResponseMessage());
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            }
        } catch (Exception e) {
            System.out.println("haha caught");
        }
    }

    public static void logout(String authToken) throws Exception{
        URI uri = new URI("http://localhost:8080/session");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setReadTimeout(5000);
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);
        http.addRequestProperty("Authorization", authToken);
        // Make the request
        http.connect();

        try {
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // Get HTTP response headers, if necessary
                // Map<String, List<String>> headers = connection.getHeaderFields();

                // OR

                //connection.getHeaderField("Content-Length");

                InputStream responseBody = http.getInputStream();
                String jsonText = IOUtils.toString(responseBody);
                // Read response body from InputStream ...
            } else {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                System.out.println(http.getResponseCode());
                System.out.println(http.getResponseMessage());
                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
            }
        } catch (Exception e) {
            System.out.println("haha caught");
        }
    }


}
