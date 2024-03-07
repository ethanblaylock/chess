package dataAccessTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;

import javax.xml.crypto.Data;
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
    @DisplayName("Clear the Application positive")
    public void clearTestPositive() throws DataAccessException {
        UserDAO.createUser(new UserData("JimBob", "password", "arwals@g.com"));
        AuthDAO.createAuth("JimBob");
        GameDAO.createGame("i love chess");
        ClearService.clearApplication();
        Assertions.assertEquals(Collections.emptySet(), UserDAO.getData());
        Assertions.assertEquals(Collections.emptySet(), AuthDAO.getData());
        Assertions.assertEquals(Collections.emptySet(), GameDAO.getData());

    }

    @Test
    @DisplayName("Clear the Application positive")
    public void getUserTestPositive() throws DataAccessException {
        UserData expectedUser = new UserData("JimBob", "password", "arwals@g.com");
        UserDAO.createUser(expectedUser);
        UserData userData = UserDAO.getUser("JimBob");
        Assertions.assertEquals(expectedUser.email(), userData.email());
        Assertions.assertEquals(expectedUser.username(), userData.username());

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
