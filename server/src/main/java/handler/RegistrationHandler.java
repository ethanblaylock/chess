package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.AuthData;
import model.UserData;
import service.RegistrationService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class RegistrationHandler {
    public static Object handleRequest(Request req, Response res) {
        Gson serializer = new Gson();
        UserData userData = serializer.fromJson(req.body(), UserData.class);

        try {
            if (userData.username() == null || userData.password() == null || userData.email() == null) {
                res.status(400);
                res.body(new DataAccessException("Bad Request").toJson());
                return res.body();
            }
            AuthData authData = RegistrationService.register(userData);
            String authJson = serializer.toJson(authData);
            res.body(authJson);
            res.status(200);
        } catch (DataAccessException error) {
            res.status(403);
            res.body(error.toJson());

        }

        return res.body();
    }
}
