package server;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;

import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WSServer {
    public final ConcurrentHashMap<String, Session> connections = new ConcurrentHashMap<>();


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
                connections.remove(Objects.requireNonNull(AuthDAO.getAuth(tempCommand.getAuthString())).username());
                for (Session session1 : connections.values()) {
                    session1.getRemote().sendString(new Gson().toJson(new Notification("Player left game: " + Objects.requireNonNull(AuthDAO.getAuth(tempCommand.getAuthString())).username())));
                }
                break;
            case RESIGN:
                for (Session session1 :  connections.values()) {
                    session1.getRemote().sendString(new Gson().toJson(new Notification(Objects.requireNonNull(AuthDAO.getAuth(tempCommand.getAuthString())).username() + " has resigned the game")));
                }
                break;
            case MAKE_MOVE:
                boolean noError = true;
                ChessMove move = new Gson().fromJson(message, MakeMove.class).getMove();
                try {
                    Objects.requireNonNull(GameDAO.getGame(1)).game().makeMove(move);
                } catch (Exception e) {
                    noError = false;
                    session.getRemote().sendString(new Gson().toJson(new Error(e.getMessage())));
                }
                if (noError) {
                    for (var session1 : connections.values()) {
                        session1.getRemote().sendString(new Gson().toJson(new LoadGame(ChessGame.TeamColor.WHITE)));
                        if (session1.isOpen()) {
                            if (session1 != session) {
                                session1.getRemote().sendString(new Gson().toJson(new Notification(AuthDAO.getAuth(tempCommand.getAuthString()).username() + " made the move: " + move)));
                            }
                        }
                    }
                }
                break;
            case JOIN_PLAYER:
                JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);

                if (GameDAO.getGame(joinPlayer.getGameID()) == null) {
                session.getRemote().sendString(new Gson().toJson(new Error("Bad Game ID")));
                }
                else if (AuthDAO.getAuth(joinPlayer.getAuthString()) == null) {
                    session.getRemote().sendString(new Gson().toJson(new Error("Bad auth")));
                }
                else if (Objects.requireNonNull(GameDAO.getGame(tempCommand.getGameID())).whiteUsername() == null && Objects.requireNonNull(GameDAO.getGame(tempCommand.getGameID())).blackUsername() == null) {
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
                    connections.put(Objects.requireNonNull(AuthDAO.getAuth(tempCommand.getAuthString())).username(), session);
                    session.getRemote().sendString(new Gson().toJson(new LoadGame(joinPlayer.getPlayerColor())));
                    for (var session1 : connections.values()) {
                        if (session1.isOpen()) {
                            if (session1 != session) {
                                session1.getRemote().sendString(new Gson().toJson(new Notification("Player Joined in Game as: " + joinPlayer.getPlayerColor())));
                            }
                        }
                    }
                }
                break;
            case JOIN_OBSERVER:


                if (GameDAO.getGame(tempCommand.getGameID()) == null) {
                    session.getRemote().sendString(new Gson().toJson(new Error("Bad Game ID")));
                }
                else if (AuthDAO.getAuth(tempCommand.getAuthString()) == null) {
                    session.getRemote().sendString(new Gson().toJson(new Error("Bad auth")));
                } else {
                    connections.put(Objects.requireNonNull(AuthDAO.getAuth(tempCommand.getAuthString())).username(), session);
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
                break;
        }



    }
}
