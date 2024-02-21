package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.Objects;

public class LoginService {
    public static AuthData login(UserData userData) throws DataAccessException {
        if (UserDAO.getUser(userData.username()) == null) {
            throw new DataAccessException("User not registered");
        }
        else if (!Objects.equals(Objects.requireNonNull(UserDAO.getUser(userData.username())).password(), userData.password())) {
            throw new DataAccessException("Wrong Password");
        }
        return AuthDAO.createAuth(userData.username());
    }
}
