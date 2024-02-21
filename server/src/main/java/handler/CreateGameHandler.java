package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import request.CreateGameRequest;
import result.CreateGameResult;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    public static Object handleRequest(Request req, Response res) {
        Gson serializer = new Gson();
        CreateGameRequest createGameRequest = serializer.fromJson(req.body(), CreateGameRequest.class);
        String authToken = req.headers("Authorization");

        try {
            if (createGameRequest.gameName() == null || authToken == null) {
                res.status(400);
                res.body(new DataAccessException("Bad Request").toJson());
                return res.body();
            }
            CreateGameResult createGameResult = CreateGameService.createGame(createGameRequest, authToken);
            res.body(serializer.toJson(createGameResult));
            res.status(200);
        } catch (DataAccessException error) {
            res.status(401);
            res.body(error.toJson());
        }
        return res.body();
    }
}
