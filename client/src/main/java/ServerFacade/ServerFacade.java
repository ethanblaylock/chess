package ServerFacade;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import model.GameData;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.CreateGameResult;
import result.ListGamesResult;
import result.RegisterResult;
import ui.EscapeSequences;

import webSocketMessages.userCommands.*;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;

import static ui.MakeBoard.makeChessBoard;

public class ServerFacade {
    WSFacade ws = null;
    public static int currentGameID;
    public static String authToken;

    public static ChessGame.TeamColor teamColor;


    public String login(String username, String password) throws Exception {
        URI uri = new URI("http://localhost:8000/session");
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
        return getString(http);
    }

    private String getString(HttpURLConnection http) {
        try {
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream responseBody = http.getInputStream();
                String jsonText = responseBody.toString();
                authToken = new Gson().fromJson(jsonText, RegisterResult.class).authToken();
                return authToken;
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

    public String register(String username, String password, String email) throws Exception {
        URI uri2 = new URI("http://localhost:8000/user");
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
        return getString(http2);
    }

    public void createGame(String gameName, String authToken) throws Exception{
        URI uri2 = new URI("http://localhost:8000/game");
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
                String jsonText = responseBody.toString();
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

    public ListGamesResult listGames(String authToken) throws Exception{
        URI uri = new URI("http://localhost:8000/game");
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
                String jsonText = responseBody.toString();
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
        return null;
    }

    public void joinGame(int gameID, ChessGame.TeamColor teamColor, String authToken, String username) throws Exception {
        URI uri3 = new URI("http://localhost:8000/game");
        HttpURLConnection http3 = (HttpURLConnection) uri3.toURL().openConnection();
        http3.setReadTimeout(5000);
        http3.setRequestMethod("PUT");
        http3.setDoOutput(true);
        // Make the request
        http3.addRequestProperty("Authorization", authToken);
        http3.connect();
        currentGameID = gameID;
        ServerFacade.teamColor = teamColor;
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

                System.out.print(EscapeSequences.RESET_TEXT_COLOR);
                if (ws == null) {
                    ws = new WSFacade();
                }
                if (teamColor != null) {
                    ws.send(new Gson().toJson(new JoinPlayer(authToken, gameID, teamColor, username)));
                } else {
                    ws.send(new Gson().toJson(new JoinObserver(authToken, gameID, username)));
                }
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

    public void logout(String authToken) throws Exception{
        URI uri = new URI("http://localhost:8000/session");
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

    public void leave() throws Exception {
        ws.send(new Gson().toJson(new Leave(authToken, currentGameID)));
    }

    public void makeMove(ChessMove move) throws Exception {
        ws.send(new Gson().toJson(new MakeMove(authToken, currentGameID, move)));
    }

    public void resign() throws Exception {
        ws.send(new Gson().toJson(new Resign(authToken, currentGameID)));
    }
    public void redrawBoard() throws Exception {
        ChessGame.TeamColor color;
        if (teamColor == ChessGame.TeamColor.BLACK) {
            color = ChessGame.TeamColor.BLACK;
        } else {
            color = ChessGame.TeamColor.WHITE;
        }
        GameData currentGame = null;
        ListGamesResult games = listGames(authToken);
        for (GameData gameData : games.games()) {
            if (gameData.gameID() == currentGameID) {
                currentGame = gameData;
            }
        }
        assert currentGame != null;
        makeChessBoard(currentGame.game().getBoard(), color);
    }

}
