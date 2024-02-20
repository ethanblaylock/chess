package handler;

import service.ClearService;
import spark.Request;
import spark.Response;


public class ClearHandler {

    public Object handleRequest(Request req, Response res) {
        ClearService.clearApplication();
        res.status(200);
        return "{}";
    }
}
