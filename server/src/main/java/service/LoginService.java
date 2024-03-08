package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import request.LoginRequest;
import result.RegisterResult;

import java.util.Objects;

public class LoginService {
    public static RegisterResult login(LoginRequest loginRequest) throws DataAccessException {
        if (UserDAO.getUser(loginRequest.username()) == null) {
            throw new DataAccessException("User not registered");
        }
        else if (!UserDAO.checkPassword(loginRequest.username(), loginRequest.password())) {
            throw new DataAccessException("Wrong Password");
        }
        AuthData authData = AuthDAO.createAuth(loginRequest.username());
        return new RegisterResult(loginRequest.username(), authData.authToken());
    }
}
