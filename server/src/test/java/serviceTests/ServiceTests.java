package serviceTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import request.CreateGameRequest;
import request.JoinGameRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.RegisterResult;
import service.*;
import dataAccess.DataAccessException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class ServiceTests {
    @BeforeEach
    public void clearData() {
        ClearService.clearApplication();
    }

    @Test
    @DisplayName("Clear the Application positive")
    public void clearTestPositive() throws DataAccessException {
        UserDAO.createUser(new UserData("JimBob", "password", "ilovenarwals@g.com"));
        AuthDAO.createAuth("JimBob");
        GameDAO.createGame("i love chess");
        ClearService.clearApplication();
        Assertions.assertEquals(Collections.emptySet(), UserDAO.getData());
        Assertions.assertEquals(Collections.emptySet(), AuthDAO.getData());
        Assertions.assertEquals(Collections.emptySet(), GameDAO.getData());

    }

    @Test
    @DisplayName("Clear the Application negative")
    public void clearTestNegative() throws DataAccessException {
        UserDAO.createUser(new UserData("JimBob", "password", "ilovenarwals@g.com"));
        AuthDAO.createAuth("JimBob");
        GameDAO.createGame("i love chess");
        Assertions.assertNotEquals(Collections.emptySet(), UserDAO.getData());
        Assertions.assertNotEquals(Collections.emptySet(), AuthDAO.getData());
        Assertions.assertNotEquals(Collections.emptySet(), GameDAO.getData());
        ClearService.clearApplication();
        Assertions.assertEquals(Collections.emptySet(), UserDAO.getData());
        Assertions.assertEquals(Collections.emptySet(), AuthDAO.getData());
        Assertions.assertEquals(Collections.emptySet(), GameDAO.getData());

    }

    @Test
    @DisplayName("Register a User")
    public void registrationTestPositive() throws DataAccessException {
        RegisterRequest registerRequest = new RegisterRequest("JimBob", "password", "ilovenarwals@g.com");

        RegistrationService.register(registerRequest);
        Collection<UserData> expected = new HashSet<>();
        expected.add(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        Assertions.assertEquals(expected, UserDAO.getData());

    }

    @Test
    @DisplayName("Username Taken")
    public void registrationTestNegative() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        RegisterRequest registerRequest = new RegisterRequest(userData.username(), userData.password(), userData.email());
        RegistrationService.register(registerRequest);

        Assertions.assertThrows(DataAccessException.class, () -> RegistrationService.register(registerRequest));

    }

    @Test
    @DisplayName("Login user")
    public void loginTestPositive() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        LoginRequest loginRequest = new LoginRequest(userData.username(), userData.password());
        RegistrationService.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        String authToken = LoginService.login(loginRequest).authToken();
        AuthData expected = new AuthData(authToken, userData.username());
        Assertions.assertTrue(AuthDAO.getData().contains(expected));
    }

    @Test
    @DisplayName("Wrong Password")
    public void loginTestNegative() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        RegistrationService.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        Assertions.assertThrows(DataAccessException.class, () -> LoginService.login(new LoginRequest(userData.username(), "not password")));

    }

    @Test
    @DisplayName("Logout User")
    public void logoutTestPositive() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        RegisterResult registerResult = RegistrationService.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        LogoutService.logout(registerResult.authToken());
        AuthData authData = new AuthData(registerResult.authToken(), registerResult.username());
        Assertions.assertFalse(AuthDAO.getData().contains(authData));
    }

    @Test
    @DisplayName("Logout without authorization")
    public void logoutTestNegative() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        RegistrationService.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        Assertions.assertThrows(DataAccessException.class, () -> LogoutService.logout("totally an authToken"));

    }

    @Test
    @DisplayName("Lists the Games")
    public void listGamesTestPositive() throws DataAccessException {
        String authToken = RegistrationService.register(new RegisterRequest("JimBob", "password", "ilovenarwals@g.com")).authToken();
        Collection<GameData> testCollection = new HashSet<>();
        GameDAO.createGame("test game");
        GameData gameData = new GameData(1,null, null,"test game", Objects.requireNonNull(GameDAO.getGame(1)).game());
        testCollection.add(gameData);
        Assertions.assertEquals(testCollection, ListGamesService.listGames(authToken).games());
    }

    @Test
    @DisplayName("Unauthorized")
    public void listGamesTestNegative() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        RegistrationService.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        Assertions.assertThrows(DataAccessException.class, () -> ListGamesService.listGames("totally an authToken"));

    }

    @Test
    @DisplayName("Game is stored in DAO")
    public void createGameTestPositive() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        String authToken = RegistrationService.register(new RegisterRequest(userData.username(), userData.password(), userData.email())).authToken();
        CreateGameService.createGame(new CreateGameRequest("test game"), authToken);
        Assertions.assertNotNull(GameDAO.getData());

    }

    @Test
    @DisplayName("create game not authorized")
    public void createGameTestNegative() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        RegistrationService.register(new RegisterRequest(userData.username(), userData.password(), userData.email()));
        Assertions.assertThrows(DataAccessException.class, () -> CreateGameService.createGame(new CreateGameRequest("test game"), "totally an authToken"));

    }

    @Test
    @DisplayName("Spot is claimed")
    public void joinGameTestPositive() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        String authToken = RegistrationService.register(new RegisterRequest(userData.username(), userData.password(), userData.email())).authToken();
        GameDAO.createGame("test game");
        JoinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 1), authToken);
        Assertions.assertEquals("JimBob", Objects.requireNonNull(GameDAO.getGame(1)).blackUsername());
    }

    @Test
    @DisplayName("Spot was already taken")
    public void joinGameTestNegative() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        String authToken = RegistrationService.register(new RegisterRequest(userData.username(), userData.password(), userData.email())).authToken();
        GameDAO.createGame("test game");
        GameDAO.updateGame(new GameData(1, "white", "NotJimBob", "test game", null));
        Assertions.assertThrows(DataAccessException.class, () -> JoinGameService.joinGame(new JoinGameRequest(ChessGame.TeamColor.BLACK, 1), authToken));

    }


}
