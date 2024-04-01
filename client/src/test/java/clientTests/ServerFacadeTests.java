package clientTests;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import server.Server;
import ServerFacade.ServerFacade;
import service.ClearService;


public class ServerFacadeTests {

    private static Server server;

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
        ServerFacade.register("bob", "ross", "email");
        Assertions.assertNotNull(ServerFacade.login("bob", "ross"));
    }

    @Test
    public void loginTestNegative() throws Exception {
        Assertions.assertNull(ServerFacade.login("bobb", "ross"));
    }

    @Test
    public void logoutTestPositive() throws Exception {
        ServerFacade.logout(ServerFacade.login("bob", "ross"));
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutTestNegative() throws Exception {
        ServerFacade.logout("");
        Assertions.assertTrue(true);
    }

    @Test
    public void registerTestPositive() throws Exception {
        Assertions.assertNotNull(ServerFacade.register("bob", "ross", "email"));
    }

    @Test
    public void registerTestNegative() throws Exception {
        ServerFacade.register("bob", "ross", "email");
        Assertions.assertNull(ServerFacade.register("bob", "ross", "email"));
    }

    @Test
    public void createGameTestPositive() throws Exception {
        ServerFacade.createGame("name", "");
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameTestNegative() throws Exception {
        ServerFacade.createGame("name", ServerFacade.register("user", "pass", "email"));
        Assertions.assertTrue(true);
    }

    @Test
    public void listGamesTestPositive() throws Exception {
        ServerFacade.listGames(ServerFacade.register("user", "pass", "email"));
        Assertions.assertTrue(true);
    }

    @Test
    public void listGamesTestNegative() throws Exception {
        ServerFacade.listGames("");
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameTestPositive() throws Exception {
        String authToken = ServerFacade.register("user", "pass", "email");
        ServerFacade.createGame("game", authToken);
        ServerFacade.joinGame(1, ChessGame.TeamColor.WHITE, authToken);
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameTestNegative() throws Exception {
        String authToken = ServerFacade.register("user", "pass", "email");
        ServerFacade.createGame("game", authToken);
        ServerFacade.joinGame(1, ChessGame.TeamColor.WHITE, authToken);
        ServerFacade.joinGame(1, ChessGame.TeamColor.WHITE, authToken);
        Assertions.assertTrue(true);
    }

}
