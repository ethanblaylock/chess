package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.RegisterRequest;
import result.RegisterResult;
import service.RegistrationService;
import spark.Request;
import spark.Response;


public class RegistrationHandler {
    public static Object handleRequest(Request req, Response res) {
        Gson serializer = new Gson();
        RegisterRequest registerRequest = serializer.fromJson(req.body(), RegisterRequest.class);

        try {
            if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
                res.status(400);
                res.body(new DataAccessException("Bad Request").toJson());
                return res.body();
            }
            RegisterResult registerResult = RegistrationService.register(registerRequest);
            String authJson = serializer.toJson(registerResult);
            res.body(authJson);
            res.status(200);
        } catch (DataAccessException error) {
            res.status(403);
            res.body(error.toJson());

        }

        return res.body();
    }
}
