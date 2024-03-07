package dataAccess;

import com.google.gson.Gson;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

public class UserDAO {
    private static final Collection<UserData> data = new HashSet<>();
    private static final Gson serializer = new Gson();
    private static final String tableName = "userData";
    /**
     * Creates a user given a UserData object
     * @param userData a UserData object to be inserted into the database
     * @throws DataAccessException throws exception if username is taken
     */
    public static void createUser(UserData userData) throws DataAccessException {
        if (getUser(userData.username()) != null) {
            throw new DataAccessException("Username already exists");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(userData.password());
        userData = new UserData(userData.username(), hashedPassword, userData.email());
        String userJson = serializer.toJson(userData);
        DatabaseManager.createDatabase();
        DatabaseManager.createTable(tableName);
        DatabaseManager.executeInsert(tableName, userJson);
    }

    /**
     * Finds the UserData object for a given username
     * @param username a username to find the data for
     * @return A UserData object corresponding to the username
     */
    public static UserData getUser(String username) throws DataAccessException {
        Collection<UserData> userData = getData();
        for (UserData user : userData) {
            if (Objects.equals(user.username(), username)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Clears all user data
     */
    public static void clear() throws DataAccessException {
        DatabaseManager.truncateTable(tableName);
    }

    /**
     * Retrieves all the data
     * @return a Collection with all the data
     */
    public static Collection<UserData> getData() throws DataAccessException {
        Collection<String> stringData = DatabaseManager.returnTable(tableName);
        Collection<UserData> userData = new HashSet<>();
        for (String userJson : stringData) {
            userData.add(serializer.fromJson(userJson, UserData.class));
        }
        return userData;
    }
}
