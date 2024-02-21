package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import model.GameData;
import request.JoinGameRequest;

import java.util.Objects;

public class JoinGameService {
    public static void joinGame(JoinGameRequest joinGameRequest, String authToken) throws DataAccessException {
        if (AuthDAO.getAuth(authToken) != null) {
            GameData currentGame = GameDAO.getGame(joinGameRequest.gameID());
            if (joinGameRequest.playerColor() != null) {
                switch (joinGameRequest.playerColor()) {
                    case WHITE:
                        assert currentGame != null;
                        if (currentGame.whiteUsername() != null) {
                            throw new DataAccessException("already taken");
                        }
                        String whiteUsername = Objects.requireNonNull(AuthDAO.getAuth(authToken)).username();
                        GameDAO.updateGame(new GameData(joinGameRequest.gameID(), whiteUsername, currentGame.blackUsername(), currentGame.gameName(), currentGame.game()));
                        break;
                    case BLACK:
                        assert currentGame != null;
                        if (currentGame.blackUsername() != null) {
                            throw new DataAccessException("already taken");
                        }
                        String blackUsername = Objects.requireNonNull(AuthDAO.getAuth(authToken)).username();
                        GameDAO.updateGame(new GameData(joinGameRequest.gameID(), currentGame.whiteUsername(), blackUsername, currentGame.gameName(), currentGame.game()));
                        break;
                }
            }
        } else {
            throw new DataAccessException("unauthorized");
        }
    }
}
