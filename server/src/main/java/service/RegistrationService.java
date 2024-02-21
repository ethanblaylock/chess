package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;


public class RegistrationService {
    public static AuthData register(UserData userData) throws DataAccessException {
        UserDAO.createUser(userData);
        return AuthDAO.createAuth(userData.username());
    }
}
