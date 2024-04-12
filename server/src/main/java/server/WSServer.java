package server;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;

@WebSocket
public class WSServer {
    public final ConcurrentHashMap<String, Session> connections = new ConcurrentHashMap<>();
    GameData currentGame;
    ChessGame game;
    public WSServer() {}

    @OnWebSocketClose
    public void onClose(Session session, int g, String msg) {
        System.out.println("This is the message form closingu: " + msg + "This is the code: " + g);
        System.out.println(session.isOpen());
    }

    @OnWebSocketError
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
        throwable.printStackTrace();
        System.out.println(("eroaosdfk"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("happy ahsdafl asldkfasldkj");
        System.out.println(session.isOpen());
        UserGameCommand tempCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch(tempCommand.getCommandType()) {
            case LEAVE:
                leave(session, message);
                break;
            case RESIGN:
                resign(session, message);
                break;
            case MAKE_MOVE:
                makeMove(session, message);
                break;
            case JOIN_PLAYER:
                joinPlayer(session, message);
                break;
            case JOIN_OBSERVER:
                joinObserver(session, message);
                break;
        }






    }
    private void leave(Session session, String message) throws DataAccessException, IOException {
        Leave leave = new Gson().fromJson(message, Leave.class);
        currentGame = GameDAO.getGame(leave.getGameID());
        assert currentGame != null;
        if (Objects.equals(currentGame.whiteUsername(), Objects.requireNonNull(AuthDAO.getAuth(leave.getAuthString())).username())) {
            assert currentGame != null;
            GameDAO.updateGame(new GameData(leave.getGameID(), null, currentGame.blackUsername(), currentGame.gameName(), currentGame.game()));
        } else if (Objects.equals(currentGame.blackUsername(), Objects.requireNonNull(AuthDAO.getAuth(leave.getAuthString())).username())) {
            assert currentGame != null;
            GameDAO.updateGame(new GameData(leave.getGameID(), currentGame.whiteUsername(), null, currentGame.gameName(), currentGame.game()));
        }
        connections.remove(Objects.requireNonNull(AuthDAO.getAuth(leave.getAuthString())).username());
        for (Session session1 : connections.values()) {
            session1.getRemote().sendString(new Gson().toJson(new Notification("Player left game: " + Objects.requireNonNull(AuthDAO.getAuth(leave.getAuthString())).username())));
        }
    }

    private void makeMove(Session session, String message) throws IOException, DataAccessException {
        MakeMove makeMove = new Gson().fromJson(message, MakeMove.class);
        boolean noError = true;
        try {
            currentGame = GameDAO.getGame(makeMove.getGameID());
            assert currentGame != null;
            game = currentGame.game();
            game.makeMove(makeMove.getMove());
            GameDAO.updateGame(new GameData(makeMove.getGameID(), currentGame.whiteUsername(), currentGame.blackUsername(), currentGame.gameName(), game));
            Objects.requireNonNull(GameDAO.getGame(makeMove.getGameID())).game().makeMove(makeMove.getMove());
        } catch (Exception e) {
            noError = false;
            session.getRemote().sendString(new Gson().toJson(new Error(e.getMessage())));
        }
        if (noError) {
            for (var session1 : connections.values()) {
                session1.getRemote().sendString(new Gson().toJson(new LoadGame(ChessGame.TeamColor.WHITE)));
                if (session1.isOpen()) {
                    if (session1 != session) {
                        session1.getRemote().sendString(new Gson().toJson(new Notification(AuthDAO.getAuth(makeMove.getAuthString()).username() + " made the move: " + makeMove.getMove())));
                    }
                }
            }
        }
    }

    private void resign(Session session, String message) throws DataAccessException, IOException {
        Resign res = new Gson().fromJson(message, Resign.class);
        currentGame = GameDAO.getGame(res.getGameID());
        assert currentGame != null;
        game = currentGame.game();
        game.endGame();
        GameDAO.updateGame(new GameData(res.getGameID(), currentGame.whiteUsername(), currentGame.blackUsername(), currentGame.gameName(), game));

        for (Session session1 :  connections.values()) {
            session1.getRemote().sendString(new Gson().toJson(new Notification(Objects.requireNonNull(AuthDAO.getAuth(res.getAuthString())).username() + " has resigned the game")));
        }
    }

    private void joinPlayer(Session session, String message) throws IOException, DataAccessException {
        JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);

        if (GameDAO.getGame(joinPlayer.getGameID()) == null) {
            session.getRemote().sendString(new Gson().toJson(new Error("Bad Game ID")));
        }
        else if (AuthDAO.getAuth(joinPlayer.getAuthString()) == null) {
            session.getRemote().sendString(new Gson().toJson(new Error("Bad auth")));
        }
        else if (Objects.requireNonNull(GameDAO.getGame(joinPlayer.getGameID())).whiteUsername() == null && Objects.requireNonNull(GameDAO.getGame(joinPlayer.getGameID())).blackUsername() == null) {
            session.getRemote().sendString(new Gson().toJson(new Error("Empty Team")));
        }
        else if (joinPlayer.getPlayerColor() == ChessGame.TeamColor.WHITE && !Objects.equals(Objects.requireNonNull(GameDAO.getGame(joinPlayer.getGameID())).whiteUsername(), Objects.requireNonNull(AuthDAO.getAuth(joinPlayer.getAuthString())).username())) {
            session.getRemote().sendString(new Gson().toJson(new Error("Place taken")));
        }
        else if (joinPlayer.getPlayerColor() == ChessGame.TeamColor.BLACK && !Objects.equals(Objects.requireNonNull(GameDAO.getGame(joinPlayer.getGameID())).blackUsername(), Objects.requireNonNull(AuthDAO.getAuth(joinPlayer.getAuthString())).username())) {
            session.getRemote().sendString(new Gson().toJson(new Error("Place taken")));
        }

        else if (joinPlayer.getPlayerColor() == ChessGame.TeamColor.WHITE && !Objects.equals(Objects.requireNonNull(GameDAO.getGame(joinPlayer.getGameID())).whiteUsername(), Objects.requireNonNull(AuthDAO.getAuth(joinPlayer.getAuthString())).username())) {
            session.getRemote().sendString(new Gson().toJson(new Error("Bad Game ID")));
        }
        else if (joinPlayer.getPlayerColor() == ChessGame.TeamColor.BLACK && !Objects.equals(Objects.requireNonNull(GameDAO.getGame(joinPlayer.getGameID())).blackUsername(), Objects.requireNonNull(AuthDAO.getAuth(joinPlayer.getAuthString())).username())) {
            session.getRemote().sendString(new Gson().toJson(new Error("Bad Game ID")));
        }

        else {
            connections.put(Objects.requireNonNull(AuthDAO.getAuth(joinPlayer.getAuthString())).username(), session);
            session.getRemote().sendString(new Gson().toJson(new LoadGame(joinPlayer.getPlayerColor())));
            for (var session1 : connections.values()) {
                if (session1.isOpen()) {
                    if (session1 != session) {
                        session1.getRemote().sendString(new Gson().toJson(new Notification("Player Joined in Game as: " + joinPlayer.getPlayerColor())));
                    }
                }
            }
        }
    }

    private void joinObserver(Session session, String message) throws IOException, DataAccessException {
        JoinObserver joinObserver = new Gson().fromJson(message, JoinObserver.class);
        if (GameDAO.getGame(joinObserver.getGameID()) == null) {
            session.getRemote().sendString(new Gson().toJson(new Error("Bad Game ID")));
        }
        else if (AuthDAO.getAuth(joinObserver.getAuthString()) == null) {
            session.getRemote().sendString(new Gson().toJson(new Error("Bad auth")));
        } else {
            connections.put(Objects.requireNonNull(AuthDAO.getAuth(joinObserver.getAuthString())).username(), session);
            session.getRemote().sendString(new Gson().toJson(new LoadGame(ChessGame.TeamColor.WHITE)));

            for (var session1 : connections.values()) {
                if (session1.isOpen()) {
                    if (session1 != session) {
                        System.out.println(session.isOpen());
                        session1.getRemote().sendString(new Gson().toJson(new Notification("Player Joined in Game as: observer")));
                    }
                }
            }
        }
    }
}
