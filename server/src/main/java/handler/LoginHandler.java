package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.LoginRequest;
import service.LoginService;
import spark.Request;
import spark.Response;



public class LoginHandler {
    public static Object handleRequest(Request req, Response res) {
        Gson serializer = new Gson();
        LoginRequest loginRequest = serializer.fromJson(req.body(), LoginRequest.class);

        try {
            if (loginRequest.username() == null || loginRequest.password() == null) {
                res.status(400);
                res.body(new DataAccessException("Bad Request").toJson());
                return res.body();
            }
            String authJson = serializer.toJson(LoginService.login(loginRequest));
            res.body(authJson);
            res.status(200);
        } catch (DataAccessException error) {
            res.status(401);
            res.body(error.toJson());

        }

        return res.body();
    }
}
