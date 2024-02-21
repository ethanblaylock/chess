package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import request.CreateGameRequest;
import result.CreateGameResult;

public class CreateGameService {
    public static CreateGameResult createGame(CreateGameRequest createGameRequest, String authToken) throws DataAccessException {
        if (AuthDAO.getAuth(authToken) != null) {
            return new CreateGameResult(GameDAO.createGame(createGameRequest.gameName()).gameID());
        } else {
            throw new DataAccessException("unauthorized");
        }
    }
}
