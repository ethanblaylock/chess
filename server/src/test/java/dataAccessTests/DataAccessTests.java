package dataAccessTests;

import dataAccess.*;
import model.AuthData;
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
    @DisplayName("Get users")
    public void getAuthTestPositive() throws DataAccessException {
        UserData expectedUser = new UserData("JimBob", "password", "arwals@g.com");
        UserDAO.createUser(expectedUser);
        UserData userData = UserDAO.getUser("JimBob");
        assert userData != null;
        Assertions.assertEquals(expectedUser.email(), userData.email());
        Assertions.assertEquals(expectedUser.username(), userData.username());
    }

    @Test
    @DisplayName("No users")
    public void getAuthTestNegative() throws DataAccessException {
        UserData userData = UserDAO.getUser("JimBob");
        Assertions.assertNull(userData);
    }

    @Test
    @DisplayName("tdads")
    public void getTableSize() throws DataAccessException {
        UserData expectedUser = new UserData("JimBob", "password", "arwals@g.com");
        UserData expectedUser2 = new UserData("Jimdob", "pasdsword", "aarwals@g.com");
        UserData expectedUser3 = new UserData("JaimBob", "passwohrd", "arw4als@g.com");
        UserDAO.createUser(expectedUser);
        UserDAO.createUser(expectedUser2);
        UserDAO.createUser(expectedUser3);
        int size = DatabaseManager.getTableSize("userData");
        Assertions.assertEquals(3, size);

    }
}
