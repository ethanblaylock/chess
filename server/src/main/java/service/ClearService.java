package service;

import dataAccess.AuthDAO;
import dataAccess.GameDAO;
import dataAccess.UserDAO;

public class ClearService {
    public static void clearApplication() {
        UserDAO.clear();
        AuthDAO.clear();
        GameDAO.clear();
    }
}
