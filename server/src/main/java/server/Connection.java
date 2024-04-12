package server;

import org.eclipse.jetty.websocket.api.*;

public record Connection(Session session, int gameID) {
}
