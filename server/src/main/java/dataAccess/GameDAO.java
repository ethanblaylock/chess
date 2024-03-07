package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.util.*;

public class GameDAO {
    private static final Collection<GameData> data = new HashSet<>();
    private static final Gson serializer = new Gson();
    private static final String tableName = "gameData";

    /**
     * Create a gameData object and gameID
     * @param gameName A name corresponding to the game
     * @return A gameData object containing the new gameID
     * @throws DataAccessException if the gameName is already being used
     */
    public static GameData createGame(String gameName) throws DataAccessException {
        for (GameData game : getData()) {
            if (Objects.equals(game.gameName(), gameName)) {
                throw new DataAccessException("Games with that name already exists");
            }
        }
        int gameID = DatabaseManager.getTableSize(tableName) + 1;
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        data.add(gameData);
        String gameJson = serializer.toJson(gameData);
        DatabaseManager.createDatabase();
        DatabaseManager.createTable(tableName);
        DatabaseManager.executeInsert(tableName, gameJson);
        return gameData;
    }

    /**
     * Retrieves the game from the unique gameID
     * @param gameID A unique int corresponding to the game
     * @return the gameData corresponding to the gameID
     */
    public static GameData getGame(int gameID) throws DataAccessException {
        for (GameData game : getData()) {
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
            String gameJson = serializer.toJson(gameData);
            DatabaseManager.deleteData(tableName, gameJson);
        } catch(DataAccessException error) {
            throw new DataAccessException("Tried to update a game that does not exist");
        }
        String gameJson = serializer.toJson(gameData);
        DatabaseManager.executeInsert(tableName, gameJson);
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
            String gameJson = serializer.toJson(gameData);
            DatabaseManager.deleteData(tableName, gameJson);
        } else {
            throw new DataAccessException("Tried to delete a game that does not exist");
        }
    }

    /**
     * Clears all the gameData
     */
    public static void clear() throws DataAccessException {
        DatabaseManager.truncateTable(tableName);
    }

    /**
     * Retrieves all the data
     * @return a Collection with all the data
     */
    public static Collection<GameData> getData() throws DataAccessException {
        Collection<String> stringData = DatabaseManager.returnTable(tableName);
        Collection<GameData> gameData = new HashSet<>();
        for (String gameJson : stringData) {
            gameData.add(serializer.fromJson(gameJson, GameData.class));
        }
        return gameData;
    }
}
