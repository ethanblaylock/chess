package handler;

import dataAccess.DataAccessException;

import org.springframework.security.core.parameters.P;
import service.LogoutService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class LogoutHandler {
    public static Object handleRequest(Request req, Response res) {
        String authToken = req.headers("Authorization");

        try {
            if (authToken == null) {
                res.status(400);
                res.body(new DataAccessException("Bad Request").toJson());
                return res.body();
            }
            LogoutService.logout(authToken);
            res.status(200);
            res.body("{}");
        } catch (DataAccessException error) {
            if (Objects.equals(error.getMessage(), "unauthorized")) {
                res.status(401);
                res.body(error.toJson());
            } else {
                res.status(500);
                res.body(error.toJson());
            }
        }
        return res.body();
    }
}
