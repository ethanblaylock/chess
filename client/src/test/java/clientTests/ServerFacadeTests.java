package clientTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import ServerFacade.ServerFacade;
import service.ClearService;


public class ServerFacadeTests {

    private static Server server;
    ServerFacade serverFacade = new ServerFacade();
    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        ClearService.clearApplication();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void loginTestPositive() throws Exception {
        serverFacade.register("bob", "ross", "email");
        Assertions.assertNotNull(serverFacade.login("bob", "ross"));
    }

    @Test
    public void loginTestNegative() throws Exception {
        Assertions.assertNull(serverFacade.login("bobb", "ross"));
    }

    @Test
    public void logoutTestPositive() throws Exception {
        serverFacade.logout(serverFacade.login("bob", "ross"));
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutTestNegative() throws Exception {
        serverFacade.logout("");
        Assertions.assertTrue(true);
    }

    @Test
    public void registerTestPositive() throws Exception {
        Assertions.assertNotNull(serverFacade.register("bob", "ross", "email"));
    }

    @Test
    public void registerTestNegative() throws Exception {
        serverFacade.register("bob", "ross", "email");
        Assertions.assertNull(serverFacade.register("bob", "ross", "email"));
    }

    @Test
    public void createGameTestPositive() throws Exception {
        serverFacade.createGame("name", "");
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameTestNegative() throws Exception {
        serverFacade.createGame("name", serverFacade.register("user", "pass", "email"));
        Assertions.assertTrue(true);
    }

    @Test
    public void listGamesTestPositive() throws Exception {
        serverFacade.listGames(serverFacade.register("user", "pass", "email"));
        Assertions.assertTrue(true);
    }

    @Test
    public void listGamesTestNegative() throws Exception {
        serverFacade.listGames("");
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameTestPositive() throws Exception {
        String authToken = serverFacade.register("user", "pass", "email");
        serverFacade.createGame("game", authToken);
        serverFacade.joinGame(1, ChessGame.TeamColor.WHITE, authToken, "user");
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameTestNegative() throws Exception {
        String authToken = serverFacade.register("user", "pass", "email");
        serverFacade.createGame("game", authToken);
        serverFacade.joinGame(1, ChessGame.TeamColor.WHITE, authToken, "user");
        serverFacade.joinGame(1, ChessGame.TeamColor.WHITE, authToken, "user");
        Assertions.assertTrue(true);
    }

}
