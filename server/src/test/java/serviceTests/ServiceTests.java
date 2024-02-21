package serviceTests;

import dataAccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.ClearService;
import service.LoginService;
import service.LogoutService;
import service.RegistrationService;
import dataAccess.DataAccessException;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public class ServiceTests {
    @BeforeEach
    public void clearData() {
        ClearService.clearApplication();
    }

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

    @Test
    @DisplayName("Register a User")
    public void registrationTestPositive() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");

        RegistrationService.register(userData);
        Collection<UserData> expected = new HashSet<>();
        expected.add(userData);
        Assertions.assertEquals(expected, UserDAO.getData());

    }

    @Test
    @DisplayName("Username Taken")
    public void registrationTestNegative() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");

        RegistrationService.register(userData);

        Assertions.assertThrows(DataAccessException.class, () -> RegistrationService.register(userData));

    }

    @Test
    @DisplayName("Login user")
    public void loginTestPositive() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        RegistrationService.register(userData);
        String authToken = LoginService.login(userData).authToken();
        AuthData expected = new AuthData(authToken, userData.username());
        Assertions.assertTrue(AuthDAO.getData().contains(expected));
    }

    @Test
    @DisplayName("Wrong Password")
    public void loginTestNegative() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        RegistrationService.register(userData);
        UserData badUserData = new UserData(userData.username(), "notpassword","ilovenarwals@g.com");
        Assertions.assertThrows(DataAccessException.class, () -> LoginService.login(badUserData));

    }

    @Test
    @DisplayName("Logout User")
    public void logoutTestPositive() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        AuthData authData= RegistrationService.register(userData);
        LogoutService.logout(authData.authToken());
        Assertions.assertFalse(AuthDAO.getData().contains(authData));
    }

    @Test
    @DisplayName("Unauthorized")
    public void logoutTestNegative() throws DataAccessException {
        UserData userData = new UserData("JimBob", "password", "ilovenarwals@g.com");
        RegistrationService.register(userData);
        Assertions.assertThrows(DataAccessException.class, () -> LogoutService.logout("totally an authToken"));

    }


}
