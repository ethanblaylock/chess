package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.annotations.*;


import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import spark.Spark;
import spark.Spark;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.userCommands.*;


import javax.websocket.EndpointConfig;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@WebSocket
public class WSServer {
    private Collection<Session> currentSessions = new HashSet<>();

    public WSServer() {}

    @OnWebSocketClose
    public void onClose(Session session, int g, String msg) {
    }

    @OnWebSocketError
    public void onError(Throwable throwable) {
        System.out.println(throwable.getMessage());
        System.out.println(("eroaosdfk"));
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        System.out.println("happy ahsdafl asldkfasldkj");
        UserGameCommand tempCommand = new Gson().fromJson(message, UserGameCommand.class);
        switch(tempCommand.getCommandType()) {
            case LEAVE:
                currentSessions.remove(session);
                for (Session session1 : currentSessions) {
                    session1.getRemote().sendString(new Gson().toJson(new Notification("Player left game: " + AuthDAO.getAuth(tempCommand.getAuthString()).username())));
                }
                break;
            case RESIGN:
                for (Session session1 : currentSessions) {
                    session1.getRemote().sendString(new Gson().toJson(new Notification(AuthDAO.getAuth(tempCommand.getAuthString()).username() + " has resigned the game")));
                }
                break;
            case MAKE_MOVE:
                for (Session session1 : currentSessions) {
                    session1.getRemote().sendString(new Gson().toJson(new LoadGame(ChessGame.TeamColor.WHITE)));
                    if (session1 != session) {
                        session1.getRemote().sendString(new Gson().toJson(new Notification(AuthDAO.getAuth(tempCommand.getAuthString()).username() + " made the move: " + new Gson().fromJson(message, MakeMove.class).getMove())));
                    }
                }
                break;
            case JOIN_PLAYER:
                JoinPlayer joinPlayer = new Gson().fromJson(message, JoinPlayer.class);
                currentSessions.add(session);
                if (GameDAO.getGame(joinPlayer.getGameID()) == null) {
                session.getRemote().sendString(new Gson().toJson(new Error("Bad Game ID")));
                }
                else if (AuthDAO.getAuth(joinPlayer.getAuthString()) == null) {
                    session.getRemote().sendString(new Gson().toJson(new Error("Bad auth")));
                }
                else if (GameDAO.getGame(tempCommand.getGameID()).whiteUsername() == null && GameDAO.getGame(tempCommand.getGameID()).blackUsername() == null) {
                    session.getRemote().sendString(new Gson().toJson(new Error("Empty Team")));
                }
                else if (joinPlayer.getPlayerColor() == ChessGame.TeamColor.WHITE && !Objects.equals(GameDAO.getGame(joinPlayer.getGameID()).whiteUsername(), AuthDAO.getAuth(joinPlayer.getAuthString()).username())) {
                    session.getRemote().sendString(new Gson().toJson(new Error("Place taken")));
                }
                else if (joinPlayer.getPlayerColor() == ChessGame.TeamColor.BLACK && !Objects.equals(GameDAO.getGame(joinPlayer.getGameID()).blackUsername(), AuthDAO.getAuth(joinPlayer.getAuthString()).username())) {
                    session.getRemote().sendString(new Gson().toJson(new Error("Place taken")));
                }

                else if (joinPlayer.getPlayerColor() == ChessGame.TeamColor.WHITE && !Objects.equals(GameDAO.getGame(joinPlayer.getGameID()).whiteUsername(), AuthDAO.getAuth(joinPlayer.getAuthString()).username())) {
                    session.getRemote().sendString(new Gson().toJson(new Error("Bad Game ID")));
                }
                else if (joinPlayer.getPlayerColor() == ChessGame.TeamColor.BLACK && !Objects.equals(GameDAO.getGame(joinPlayer.getGameID()).blackUsername(), AuthDAO.getAuth(joinPlayer.getAuthString()).username())) {
                    session.getRemote().sendString(new Gson().toJson(new Error("Bad Game ID")));
                }

                else {
                    session.getRemote().sendString(new Gson().toJson(new LoadGame(joinPlayer.getPlayerColor())));
                    for (Session session1 : currentSessions) {
                        if (session1 != session) {
                            session1.getRemote().sendString(new Gson().toJson(new Notification("Player Joined in Game as: " + joinPlayer.getPlayerColor())));
                        }
                    }
                }
                break;
            case JOIN_OBSERVER:
                currentSessions.add(session);
                if (GameDAO.getGame(tempCommand.getGameID()) == null) {
                    session.getRemote().sendString(new Gson().toJson(new Error("Bad Game ID")));
                }
                else if (AuthDAO.getAuth(tempCommand.getAuthString()) == null) {
                    session.getRemote().sendString(new Gson().toJson(new Error("Bad auth")));
                } else {
                    session.getRemote().sendString(new Gson().toJson(new LoadGame(ChessGame.TeamColor.WHITE)));

                    for (Session session1 : currentSessions) {
                        if (session1 != session) {
                            session1.getRemote().sendString(new Gson().toJson(new Notification("Player Joined in Game as: observer")));
                        }
                    }
                }
                break;
        }



    }
}
