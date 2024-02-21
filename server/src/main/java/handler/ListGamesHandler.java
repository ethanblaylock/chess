package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import service.ListGamesService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class ListGamesHandler {
    public static Object handleRequest(Request req, Response res) {
        Gson serializer = new Gson();
        String authToken = req.headers("Authorization");

        try {
            if (authToken == null) {
                res.status(400);
                res.body(new DataAccessException("Bad Request").toJson());
                return res.body();
            }

            res.status(200);
            res.body(serializer.toJson(ListGamesService.listGames(authToken)));
        } catch (DataAccessException error) {
            if (Objects.equals(error.getMessage(), "unauthorized")) {
                res.status(401);
                res.body(error.toJson());
            }
        }
        return res.body();
    }
}
