package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.LoginService;
import spark.Request;
import spark.Response;



public class LoginHandler {
    public static Object handleRequest(Request req, Response res) {
        Gson serializer = new Gson();
        UserData userData = serializer.fromJson(req.body(), UserData.class);

        try {
            if (userData.username() == null || userData.password() == null) {
                res.status(400);
                res.body(new DataAccessException("Bad Request").toJson());
                return res.body();
            }
            AuthData authData = LoginService.login(userData);
            String authJson = serializer.toJson(authData);
            res.body(authJson);
            res.status(200);
        } catch (DataAccessException error) {
            res.status(401);
            res.body(error.toJson());

        }

        return res.body();
    }
}
