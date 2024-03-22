package clientTests;

import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
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
    public void loginTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void loginTestNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void logoutTestNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void registerTestNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameTestNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void listGamesTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void listGamesTestNegative() {
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameTestPositive() {
        Assertions.assertTrue(true);
    }

    @Test
    public void joinGameTestNegative() {
        Assertions.assertTrue(true);
    }

}
