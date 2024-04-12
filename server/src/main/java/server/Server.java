package server;

import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import handler.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.*;
import spark.*;
import org.eclipse.jetty.websocket.api.annotations.*;


public class Server {
    public static void main(String[] args)  {
        new Server().run(8000);
    }

    public int run(int desiredPort) {
        try {
            DatabaseManager.createDatabase();
            DatabaseManager.createTable("userData");
            DatabaseManager.createTable("authData");
            DatabaseManager.createTable("gameData");
        } catch (DataAccessException ignored) {

        }
        WSServer wsServer = new WSServer();
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register a directory for hosting static files
        Spark.externalStaticFileLocation("public");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/connect", wsServer);
        Spark.post("/user", RegistrationHandler::handleRequest);
        Spark.post("/session", LoginHandler::handleRequest);
        Spark.delete("/session", LogoutHandler::handleRequest);
        Spark.get("/game", ListGamesHandler::handleRequest);
        Spark.post("/game", CreateGameHandler::handleRequest);
        Spark.put("/game", JoinGameHandler::handlerRequest);
        Spark.delete("/db", ClearHandler::handleRequest);





        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}
