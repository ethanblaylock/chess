package dataAccess;

import chess.ChessGame;
import model.GameData;

import java.util.*;

public class GameDAO {
    private static final Collection<GameData> data = new HashSet<>();

    /**
     * Create a gameData object and gameID
     * @param gameName A name corresponding to the game
     * @return A gameData object containing the new gameID
     * @throws DataAccessException if the gameName is already being used
     */
    public static GameData createGame(String gameName) throws DataAccessException {
        for (GameData game : data) {
            if (Objects.equals(game.gameName(), gameName)) {
                throw new DataAccessException("Games with that name already exists");
            }
        }
        int gameID = data.size() + 1;
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        data.add(gameData);
        return gameData;
    }

    /**
     * Retrieves the game from the unique gameID
     * @param gameID A unique int corresponding to the game
     * @return the gameData corresponding to the gameID
     */
    public static GameData getGame(int gameID) {
        for (GameData game : data) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    /**
     * Updates the gameData with new gameData
     * @param gameData the new data to replace to old data
     * @throws DataAccessException if the new data corresponds to a game that does not exist
     */
    public static void updateGame(GameData gameData) throws DataAccessException {
        try {
            deleteGame(gameData);
        } catch(DataAccessException error) {
            throw new DataAccessException("Tried to update a game that does not exist");
        }
        data.add(gameData);
    }

    /**
     * Deletes a particular game given by the gameData
     * @param gameData the gameData that dictates what is to be deleted
     * @throws DataAccessException if the game to be deleted does not exist
     */
    public static void deleteGame(GameData gameData) throws DataAccessException {
        if (getGame(gameData.gameID()) != null) {
            data.remove(getGame(gameData.gameID()));
        } else {
            throw new DataAccessException("Tried to delete a game that does not exist");
        }
    }

    /**
     * Clears all the gameData
     */
    public static void clear() { data.clear(); }

    /**
     * Retrieves all the data
     * @return a Collection with all the data
     */
    public static Collection<GameData> getData() { return data; }
}
