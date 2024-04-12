package server;

import chess.ChessGame;

import javax.websocket.*;

public class Connection {
    public ChessGame.TeamColor teamColor;
    public Session session;

    public Connection(ChessGame.TeamColor teamColor, Session session) {
        this.teamColor = teamColor;
        this.session = session;
    }

    public Session getSession() {return session; }
}
