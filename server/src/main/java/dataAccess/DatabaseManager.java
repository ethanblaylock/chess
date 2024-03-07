package dataAccess;

import model.UserData;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;

public class DatabaseManager {
    private static final String databaseName;
    private static final String user;
    private static final String password;
    private static final String connectionUrl;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) throw new Exception("Unable to load db.properties");
                Properties props = new Properties();
                props.load(propStream);
                databaseName = props.getProperty("db.name");
                user = props.getProperty("db.user");
                password = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                connectionUrl = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        try {
            var statement = "CREATE DATABASE IF NOT EXISTS " + databaseName;
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
                conn.setCatalog(databaseName);
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void createTable(String tableName) throws DataAccessException {
        try {
            var useStatement = "USE " + databaseName;
            var statement = "CREATE TABLE IF NOT EXISTS " + tableName + " (id int NOT NULL AUTO_INCREMENT, data mediumtext, PRIMARY KEY (id))";
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            try (var usePreparedStatement = conn.prepareStatement(useStatement)) {
                usePreparedStatement.executeUpdate();
            }
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    static void executeInsert(String tableName, String data) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO " + tableName + " (data) VALUES(?)")) {
                preparedStatement.setString(1, data);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void truncateTable(String tableName) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE " + tableName)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException((e.getMessage()));
        }
    }

    public static void deleteData(String tableName, String data) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM " + tableName + " WHERE data = " + data)) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException((e.getMessage()));
        }
    }

    public static int getTableSize(String tableName) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT data FROM " + tableName)) {
                var rs = preparedStatement.executeQuery();
                if (rs != null) {
                    int size = 0;
                    while (rs.next()) {
                        size++;
                    }
                    return size;
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException((e.getMessage()));
        }
    }

    public static Collection<String> returnTable(String tableName) throws DataAccessException {
        Collection<String> data = new HashSet<>();
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT data FROM " + tableName)) {
                var rs = preparedStatement.executeQuery();
                if (rs != null) {
                    while (rs.next()) {
                        data.add(rs.getString("data"));

                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException((e.getMessage()));
        }
        return data;
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(connectionUrl, user, password);
            conn.setCatalog(databaseName);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static void dropDatabase() throws DataAccessException {
        try (var conn = getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DROP DATABASE chess")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException((e.getMessage()));
        }
    }
}
