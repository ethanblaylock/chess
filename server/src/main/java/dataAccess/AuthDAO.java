package dataAccess;

import com.google.gson.Gson;
import model.AuthData;
import model.UserData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class AuthDAO {
    private static final Collection<AuthData> data = new HashSet<>();
    private static final Gson serializer = new Gson();
    private static final String tableName = "authData";

    /**
     * Create an authorization for a username
     * @param username a username to be associated with the authorization
     * @return A AuthData object with a unique authToken
     */
    public static AuthData createAuth(String username) throws DataAccessException {
        //data.removeIf(auth -> Objects.equals(auth.username(), username));
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        String authJson = serializer.toJson(authData);
        data.add(authData);
        DatabaseManager.createDatabase();
        DatabaseManager.createTable(tableName);
        DatabaseManager.executeInsert(tableName, authJson);
        return authData;
    }

    /**
     * Retrieves an authorization for a given authToken
     * @param authToken the authToken that is authorized
     * @return the authorization associated with the authToken or null
     */
    public static AuthData getAuth(String authToken) throws DataAccessException {
        Collection<AuthData> authData = getData();
        for (AuthData auth : authData) {
            if (Objects.equals(auth.authToken(), authToken)) {
                return auth;
            }
        }
        return null;
    }

    /**
     * Deletes a specific authorization given by the authToken
     * @param authData specifies which authorization is to be deleted
     * @throws DataAccessException if the authToken is invalid
     */
    public static void deleteAuth(AuthData authData) throws DataAccessException {
        if (getAuth(authData.authToken()) != null) {
            String authJson = serializer.toJson(authData);
            DatabaseManager.deleteData(tableName, authJson);
        } else {
            throw new DataAccessException("Authorization does not exist");
        }
    }

    /**
     * Clears all the current authorizations
     */
    public static void clear() throws DataAccessException {
        DatabaseManager.truncateTable(tableName);
    }

    /**
     * Retrieves all the data
     * @return a Collection with all the data
     */
    public static Collection<AuthData> getData() throws DataAccessException {
        Collection<String> stringData = DatabaseManager.returnTable(tableName);
        Collection<AuthData> authData = new HashSet<>();
        for (String authJson : stringData) {
            authData.add(serializer.fromJson(authJson, AuthData.class));
        }
        return authData;
    }




}
