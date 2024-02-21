package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.UserDAO;
import model.AuthData;
import model.UserData;
import request.RegisterRequest;
import result.RegisterResult;


public class RegistrationService {
    public static RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        UserDAO.createUser(new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email()));
        AuthData authData = AuthDAO.createAuth(registerRequest.username());
        return new RegisterResult(registerRequest.username(), authData.authToken());
    }
}
