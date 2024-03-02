package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;

import java.util.Collections;

public class DataAccessTests {
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
}
