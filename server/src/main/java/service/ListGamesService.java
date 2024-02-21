package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import result.ListGamesResult;


public class ListGamesService {
    public static ListGamesResult listGames(String authToken) throws DataAccessException {
        if (AuthDAO.getAuth(authToken) != null) {
            return new ListGamesResult(GameDAO.getData());
        } else {
            throw new DataAccessException("unauthorized");
        }
    }
}
