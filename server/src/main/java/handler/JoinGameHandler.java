package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.JoinGameRequest;
import service.JoinGameService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class JoinGameHandler {
    public static Object handlerRequest(Request req, Response res) {
        Gson serializer = new Gson();
        JoinGameRequest joinGameRequest = serializer.fromJson(req.body(), JoinGameRequest.class);
        String authToken = req.headers("Authorization");

        try {
            if (joinGameRequest.gameID() == 0) {
                res.status(400);
                res.body(new DataAccessException("Bad Request").toJson());
                return res.body();
            }
            JoinGameService.joinGame(joinGameRequest, authToken);
            res.body("{}");
            res.status(200);
        } catch (DataAccessException error) {
            if (Objects.equals(error.getMessage(), "unauthorized")) {
                res.status(401);
                res.body(error.toJson());
            }
            else if (Objects.equals(error.getMessage(), "already taken")) {
                res.status(403);
                res.body(error.toJson());
            } else {
                res.status(500);
                res.body(error.toJson());
            }
        }

        return res.body();

    }
}
