package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;

import java.util.Objects;

public class LogoutService {
    public static void logout(String authToken) throws DataAccessException {
        if (AuthDAO.getAuth(authToken) != null) {
            AuthDAO.deleteAuth(Objects.requireNonNull(AuthDAO.getAuth(authToken)));
        } else {
            throw new DataAccessException("unauthorized");
        }
    }
}
