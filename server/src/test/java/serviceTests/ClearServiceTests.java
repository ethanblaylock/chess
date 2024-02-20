package serviceTests;

import dataAccess.*;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;

import java.util.Collections;

public class ClearServiceTests {
    @Test
    @DisplayName("Clear the Application")
    public void clearTest() throws DataAccessException {
        UserDAO.createUser(new UserData("JimBob", "password", "ilovenarwals@g.com"));
        AuthDAO.createAuth("JimBob");
        GameDAO.createGame("ilovechess");
        ClearService.clearApplication();
        Assertions.assertEquals(Collections.emptySet(), UserDAO.getData());
        Assertions.assertEquals(Collections.emptySet(), AuthDAO.getData());
        Assertions.assertEquals(Collections.emptySet(), GameDAO.getData());

    }
}
