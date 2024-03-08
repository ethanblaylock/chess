package dataAccessTests;

import chess.ChessGame;
import dataAccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Collections;

public class DataAccessTests {

    @BeforeEach
    public void setupDatabase() throws DataAccessException {
        DatabaseManager.dropDatabase();
        DatabaseManager.createDatabase();
        DatabaseManager.createTable("userData");
        DatabaseManager.createTable("authData");
        DatabaseManager.createTable("gameData");
        DatabaseManager.truncateTable("userData");
        DatabaseManager.truncateTable("authData");
        DatabaseManager.truncateTable("gameData");
    }
    @Test
    @DisplayName("Create User")
    public void createUserPositive() throws DataAccessException {
        UserDAO.createUser(new UserData("JimBob", "password", "arwals@g.com"));
        Assertions.assertNotNull(UserDAO.getData());
    }

    @Test
    @DisplayName("Username taken")
    public void createUserNegative() throws DataAccessException {
        UserData user = new UserData("JimBob", "password", "arwals@g.com");
        UserDAO.createUser(user);
        Assertions.assertThrows(DataAccessException.class, () -> UserDAO.createUser(user));
    }

    @Test
    @DisplayName("Correct password")
    public void checkPasswordPositive() throws DataAccessException {
        UserData user = new UserData("JimBob", "password", "arwals@g.com");
        UserDAO.createUser(user);
        Assertions.assertTrue(UserDAO.checkPassword(user.username(), user.password()));
    }

    @Test
    @DisplayName("Wrong password")
    public void checkPasswordNegative() throws DataAccessException {
        UserData user = new UserData("JimBob", "password", "arwals@g.com");
        UserDAO.createUser(user);
        String fakePassword = "fake password";
        Assertions.assertFalse(UserDAO.checkPassword(user.username(), fakePassword));
    }

    @Test
    @DisplayName("Get users")
    public void getUserTestPositive() throws DataAccessException {
        UserData expectedUser = new UserData("JimBob", "password", "arwals@g.com");
        UserDAO.createUser(expectedUser);
        UserData userData = UserDAO.getUser("JimBob");
        assert userData != null;
        Assertions.assertEquals(expectedUser.email(), userData.email());
        Assertions.assertEquals(expectedUser.username(), userData.username());
    }

    @Test
    @DisplayName("No users")
    public void getUserTestNegative() throws DataAccessException {
        UserData userData = UserDAO.getUser("JimBob");
        Assertions.assertNull(userData);
    }

    @Test
    @DisplayName("Clear users")
    public void userClear() throws DataAccessException {
        UserData expectedUser = new UserData("JimBob", "password", "arwals@g.com");
        UserDAO.createUser(expectedUser);
        UserDAO.clear();
        Assertions.assertEquals(UserDAO.getData(), Collections.emptySet());
    }

    @Test
    @DisplayName("Get the Data")
    public void getUserDataPositive() throws DataAccessException {
        UserData expectedUser = new UserData("JimBob", "password", "arwals@g.com");
        UserDAO.createUser(expectedUser);
        Assertions.assertNotNull(UserDAO.getData());
    }

    @Test
    @DisplayName("No data")
    public void getUserDataNegative() throws DataAccessException {
        Assertions.assertEquals(UserDAO.getData(), Collections.emptySet());
    }

    @Test
    @DisplayName("Create Auth")
    public void createAuthPositive() throws DataAccessException {
        AuthData actual = AuthDAO.createAuth("JimBob");
        Assertions.assertNotNull(actual);
    }

    @Test
    @DisplayName("Create Auth Negative")
    public void createAuthNegative() throws DataAccessException {
        AuthData first = AuthDAO.createAuth("JimBob");
        AuthData second = AuthDAO.createAuth("JimBob");
        Assertions.assertNotEquals(first, second);
    }

    @Test
    @DisplayName("Get auths")
    public void getAuthTestPositive() throws DataAccessException {
        AuthData actual = AuthDAO.createAuth("JimBob");
        AuthData obtained = AuthDAO.getAuth(actual.authToken());
        Assertions.assertEquals(actual, obtained);
    }

    @Test
    @DisplayName("No auths")
    public void getAuthTestNegative() throws DataAccessException {
        AuthData authData = AuthDAO.getAuth("JimBob");
        Assertions.assertNull(authData);
    }

    @Test
    @DisplayName("delete auth")
    public void deleteAuthTestPositive() throws DataAccessException {
        AuthData authData = AuthDAO.createAuth("JimBob");
        AuthDAO.deleteAuth(authData);
        Assertions.assertEquals(AuthDAO.getData(), Collections.emptySet());
    }

    @Test
    @DisplayName("no auth to delete")
    public void deleteAuthTestNegative() {
        Assertions.assertThrows(DataAccessException.class, () -> AuthDAO.deleteAuth(new AuthData("fake auth", "fake username")));
    }

    @Test
    @DisplayName("Clear auths")
    public void authClear() throws DataAccessException {
        AuthDAO.createAuth("JimBob");
        AuthDAO.clear();
        Assertions.assertEquals(AuthDAO.getData(), Collections.emptySet());
    }

    @Test
    @DisplayName("Get the auth Data")
    public void getAuthDataPositive() throws DataAccessException {
        AuthDAO.createAuth("JimBob");
        Assertions.assertNotNull(AuthDAO.getData());
    }

    @Test
    @DisplayName("No auth data")
    public void getAuthDataNegative() throws DataAccessException {
        Assertions.assertEquals(AuthDAO.getData(), Collections.emptySet());
    }

    @Test
    @DisplayName("Create Game")
    public void createGamePositive() throws DataAccessException {
        GameDAO.createGame("test game");
        Assertions.assertNotNull(GameDAO.getData());
    }

    @Test
    @DisplayName("Create Game negative")
    public void createGameNegative() throws DataAccessException {
        GameDAO.createGame("test game");
        Assertions.assertThrows(DataAccessException.class, () -> GameDAO.createGame("test game"));
    }

    @Test
    @DisplayName("Get games")
    public void getGameTestPositive() throws DataAccessException {
        GameData actual = GameDAO.createGame("test game");
        GameData obtained = GameDAO.getGame(actual.gameID());
        Assertions.assertEquals(actual, obtained);
    }

    @Test
    @DisplayName("No games")
    public void getGameTestNegative() throws DataAccessException {
        GameData gameData = GameDAO.getGame(1);
        Assertions.assertNull(gameData);
    }

    @Test
    @DisplayName("Updates game")
    public void updateGameTestPositive() throws DataAccessException {
        GameData first = GameDAO.createGame("test game");
        GameData updated = new GameData(1, "not null", null, "test game", new ChessGame());
        GameDAO.updateGame(updated);
        Assertions.assertNotEquals(first, GameDAO.getGame(updated.gameID()));
    }

    @Test
    @DisplayName("Not updated")
    public void updateGameTestNegative() throws DataAccessException {
        GameData first = GameDAO.createGame("test game");
        GameData updated = new GameData(1, "not null", null, "test game", new ChessGame());
        Assertions.assertEquals(first, GameDAO.getGame(updated.gameID()));
    }

    @Test
    @DisplayName("Clear games")
    public void gameClear() throws DataAccessException {
        GameDAO.createGame("test game");
        GameDAO.clear();
        Assertions.assertEquals(GameDAO.getData(), Collections.emptySet());
    }

    @Test
    @DisplayName("Get the game Data")
    public void getGameDataPositive() throws DataAccessException {
        GameDAO.createGame("test game");
        Assertions.assertNotNull(GameDAO.getData());
    }

    @Test
    @DisplayName("No Game data")
    public void getGameDataNegative() throws DataAccessException {
        Assertions.assertEquals(GameDAO.getData(), Collections.emptySet());
    }
}
