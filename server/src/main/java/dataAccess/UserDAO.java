package dataAccess;

import model.UserData;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class UserDAO {
    private Collection<UserData> data = new HashSet<>();

    /**
     * Creates a user given a UserData object
     * @param userData a UserData object to be inserted into the database
     * @throws DataAccessException throws exception if username is taken
     */
    void createUser(UserData userData) throws DataAccessException {
        for (UserData user : data) {
            if (Objects.equals(user.username(), userData.username())) {
                throw new DataAccessException("Username already exists");
            }
        }
        data.add(userData);
    }

    /**
     * Finds the UserData object for a given username
     * @param username a username to find the data for
     * @return A UserData object corresponding to the username
     */
    UserData getUser(String username) {
        for (UserData user : data) {
            if (Objects.equals(user.username(), username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Clears all user data
     */
    void clear() { data.clear(); }
}
