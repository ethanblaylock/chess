package dataAccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class AuthDAO {
    private Collection<AuthData> data = new HashSet<>();

    /**
     * Create an authorization for a username
     * @param username a username to be associated with the authorization
     * @return A AuthData object with a unique authToken
     * @throws DataAccessException if Username is already being used for another authorization
     */
    AuthData createAuth(String username) throws DataAccessException {
        for (AuthData auth : data) {
            if (Objects.equals(auth.username(), username)) {
                throw new DataAccessException("Username is already associated with an authToken");
            }
        }
        String authToken = UUID.randomUUID().toString();
        AuthData authData = new AuthData(authToken, username);
        data.add(authData);
        return authData;
    }

    /**
     * Retrieves an authorization for a given authToken
     * @param authToken the authToken that is authorized
     * @return the authorization associated with the authToken or null
     */
    AuthData getAuth(String authToken) {
        for (AuthData auth : data) {
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
    void deleteAuth(AuthData authData) throws DataAccessException {
        if (getAuth(authData.authToken()) != null) {
            data.remove(getAuth(authData.authToken()));
        } else {
            throw new DataAccessException("Authorization does not exist");
        }
    }

    /**
     * Clears all the current authorizations
     */
    void clear() { data.clear(); }




}
