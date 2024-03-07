package service;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ClearService {
    public static void clearApplication()  {
        try {
            UserDAO.clear();
            AuthDAO.clear();
            GameDAO.clear();
        } catch (DataAccessException error) {

        }
    }
}
